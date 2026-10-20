package com.example.suibianwanwan.greet;

import com.example.greet.autoconfigure.GreetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 用户自定义 Bean，演示 @ConditionalOnMissingBean 的"用户优先"。
 * 默认不激活，使用 --spring.profiles.active=custom 时生效。
 */
@Configuration(proxyBeanMethods = false)
@Profile("custom")
public class CustomGreetConfiguration {

    @Bean
    public GreetService customGreetService() {
        return name -> "[custom] hi " + name;
    }
}
