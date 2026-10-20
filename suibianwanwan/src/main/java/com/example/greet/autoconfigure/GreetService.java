package com.example.greet.autoconfigure;

/**
 * 对外暴露的扩展点，使用方可以自己实现并注册为 Bean 来覆盖默认实现。
 */
@FunctionalInterface
public interface GreetService {

    String greet(String name);
}
