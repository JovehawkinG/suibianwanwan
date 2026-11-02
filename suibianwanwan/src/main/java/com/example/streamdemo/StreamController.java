package com.example.streamdemo;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Spring MVC 流式输出验证：基础 SSE + 容器默认超时。
 *
 * <pre>
 * curl -N http://localhost:8090/stream/sse            # 5 条，每秒一条，5 秒结束
 * curl -N http://localhost:8090/stream/sse-timeout    # 每 2 秒一条，30 秒后断
 * </pre>
 */
@RestController
@RequestMapping("/stream")
public class StreamController {

    private static final Logger log = LoggerFactory.getLogger(StreamController.class);

    /** 推送线程池。4 条够观察；生产要设队列容量和拒绝策略，别用 new Thread */
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * 基础流式：5 条消息，每秒一条，显式关掉超时。
     * 观察点：请求线程是 http-nio-8090-exec-N，推送线程是 pool-1-thread-N，
     * 说明请求线程在方法返回后已经归还 Tomcat，写数据的是另一条线程。
     */
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse() {
        log.info("[请求线程] {} 处理完立即归还 Tomcat", Thread.currentThread().getName());

        SseEmitter emitter = new SseEmitter(0L);            // 0 = 永不超时
        emitter.onCompletion(() -> log.info("[回调] onCompletion：流正常结束"));
        emitter.onError(e -> log.info("[回调] onError：{}", e.toString()));
        emitter.onTimeout(() -> log.warn("[回调] onTimeout"));

        pool.execute(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    emitter.send(SseEmitter.event()
                            .id(String.valueOf(i))
                            .name("tick")
                            .data("第 " + i + " 条 / 推送线程 " + Thread.currentThread().getName()));
                    // BREAKPOINT 1：在这里停住，看 IDEA Threads 面板里两条线程同时在
                    TimeUnit.SECONDS.sleep(1);
                }
                emitter.complete();                         // BREAKPOINT 2：走完直接结束响应
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                emitter.completeWithError(e);
            }
            catch (Exception e) {
                // 客户端中途关掉 curl 会走到这里：IOException / IllegalStateException
                log.warn("[推送中止] {}", e.toString());
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    /**
     * 默认超时对照：构造时不传 timeout，走容器默认值。
     * Tomcat 的 asyncTimeout 默认 30000ms，所以第 15 条左右连接会被切断。
     */
    @GetMapping(value = "/sse-timeout", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sseTimeout() {
        SseEmitter emitter = new SseEmitter();              // 故意不设 timeout
        emitter.onTimeout(() -> log.warn("[回调] onTimeout：容器默认超时到了，连接被切断"));
        emitter.onCompletion(() -> log.info("[回调] onCompletion"));
        emitter.onError(e -> log.info("[回调] onError：{}", e.toString()));

        pool.execute(() -> {
            try {
                for (int i = 1; ; i++) {
                    emitter.send(SseEmitter.event().data("第 " + i + " 条"));
                    TimeUnit.SECONDS.sleep(2);
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            catch (Exception e) {
                // BREAKPOINT 3：超时后下一次 send 抛 IllegalStateException，循环在这里退出
                log.warn("[推送中止] {}", e.toString());
            }
        });
        return emitter;
    }

    @PreDestroy
    void shutdown() {
        pool.shutdownNow();
    }
}
