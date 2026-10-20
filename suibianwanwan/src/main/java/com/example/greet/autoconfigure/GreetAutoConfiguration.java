package com.example.greet.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 自动配置入口，只能通过 META-INF/spring/...AutoConfiguration.imports 加载。
 *
 * <p>故意放在 com.example.greet 下，不在 @SpringBootApplication 的扫描范围内，
 * 这样一旦删掉 imports 文件，下面的 Bean 就不会被注册，机制可见。
 */
@AutoConfiguration
@EnableConfigurationProperties(GreetProperties.class)
@ConditionalOnProperty(prefix = "demo.greet", name = "enabled", matchIfMissing = true)
public class GreetAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GreetService greetService(GreetProperties properties) {
        return new DefaultGreetService(properties);
    }
}
