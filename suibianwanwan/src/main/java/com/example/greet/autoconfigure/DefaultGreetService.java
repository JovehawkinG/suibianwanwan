package com.example.greet.autoconfigure;

/**
 * 默认实现，不带 @Component，完全由 GreetAutoConfiguration 决定是否注册。
 */
public class DefaultGreetService implements GreetService {

    private final GreetProperties properties;

    public DefaultGreetService(GreetProperties properties) {
        this.properties = properties;
    }

    @Override
    public String greet(String name) {
        return properties.getPrefix() + ", " + name + properties.getSuffix();
    }
}
