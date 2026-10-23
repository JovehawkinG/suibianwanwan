package com.example.startupdemo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * Spring Boot 启动流程调试入口。
 *
 * <p>包名刻意放在 com.example.startupdemo，不在主应用 com.example.suibianwanwan
 * 的扫描路径内，启动 SuibianwanwanApplication 时不会加载本类。
 *
 * <p>运行方式：IDE 中右键 main 方法 Debug，或执行
 * <pre>
 * .\mvnw.cmd spring-boot:run -Dspring-boot.run.main-class=com.example.startupdemo.StartupFlowDemo
 * </pre>
 */
@SpringBootApplication(scanBasePackages = "com.example.startupdemo")
public class StartupFlowDemo {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StartupFlowDemo.class);
        // SERVLET 会真实启动 Tomcat，便于观察 onRefresh；改成 NONE 则启动完直接退出
        app.setWebApplicationType(WebApplicationType.SERVLET);
        // 随机端口，避免和本机 8080 冲突
        app.setDefaultProperties(Map.of("server.port", "0"));
        app.addListeners(new StageLogger());
        app.run(args);   // BREAKPOINT 1：进入后单步跟 SpringApplication#run
    }

    @Bean
    public DemoRepository demoRepository() {
        System.out.println("[bean] DemoRepository 创建");          // BREAKPOINT 2
        return new DemoRepository();
    }

    @Bean
    public DemoService demoService(DemoRepository demoRepository) {
        System.out.println("[bean] DemoService 创建，注入 " + demoRepository);  // BREAKPOINT 3
        return new DemoService(demoRepository);
    }

    @Bean
    public ApplicationRunner runnerProbe() {
        return args -> System.out.println("[runner] Runner 执行：容器已 refresh，端口已启动");
    }

    static class StageLogger implements ApplicationListener<ApplicationEvent> {
        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            System.out.println("[stage] " + event.getClass().getSimpleName());  // BREAKPOINT 4
        }
    }

    public static class DemoRepository {
    }

    public static class DemoService {

        private final DemoRepository repository;

        public DemoService(DemoRepository repository) {
            this.repository = repository;
        }

        public DemoRepository getRepository() {
            return repository;
        }
    }
}
