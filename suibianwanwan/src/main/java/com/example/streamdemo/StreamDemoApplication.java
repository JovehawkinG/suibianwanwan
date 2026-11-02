package com.example.streamdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

/**
 * SSE 流式输出 demo 入口。
 *
 * <p>独立启动，端口 8090，不会影响主应用 SuibianwanwanApplication（默认 8080）。
 * scanBasePackages 限定在本包，避免把其它 demo 包的 Bean 一起扫进来。
 *
 * <p>运行方式二选一：
 * <pre>
 * IDE 中右键本类 main 方法直接运行
 * .\mvnw.cmd spring-boot:run "-Dspring-boot.run.main-class=com.example.streamdemo.StreamDemoApplication"
 * </pre>
 */
@SpringBootApplication(scanBasePackages = "com.example.streamdemo")
public class StreamDemoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StreamDemoApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8090"));
        app.run(args);
    }
}
