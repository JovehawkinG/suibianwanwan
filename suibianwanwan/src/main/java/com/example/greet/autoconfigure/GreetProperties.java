package com.example.greet.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自动配置的属性绑定类，对应 application.properties 中的 demo.greet.*。
 */
@ConfigurationProperties(prefix = "demo.greet")
public class GreetProperties {

    /**
     * 是否启用 GreetService 自动配置，由 @ConditionalOnProperty 读取，默认 true。
     */
    private boolean enabled = true;

    private String prefix = "Hello";

    private String suffix = "!";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
