package com.example.suibianwanwan.greet;

import com.example.greet.autoconfigure.GreetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动完成后打印实际生效的实现类，便于核对自动配置结果。
 */
@Component
public class StartupReporter implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupReporter.class);

    private final ObjectProvider<GreetService> greetServiceProvider;

    public StartupReporter(ObjectProvider<GreetService> greetServiceProvider) {
        this.greetServiceProvider = greetServiceProvider;
    }

    @Override
    public void run(ApplicationArguments args) {
        GreetService service = greetServiceProvider.getIfAvailable();
        log.info("GreetService impl: {}", service == null ? "not registered" : service.getClass().getName());
    }
}
