package com.example.suibianwanwan.greet;

import com.example.greet.autoconfigure.GreetService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {

    /**
     * 用 ObjectProvider 而不是直接注入，便于演示 demo.greet.enabled=false 时应用仍能启动。
     */
    private final ObjectProvider<GreetService> greetServiceProvider;

    public GreetController(ObjectProvider<GreetService> greetServiceProvider) {
        this.greetServiceProvider = greetServiceProvider;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "Spring") String name) {
        GreetService service = greetServiceProvider.getIfAvailable();
        if (service == null) {
            return "GreetService not registered: demo.greet.enabled=false";
        }
        return service.greet(name) + " | impl=" + service.getClass().getSimpleName();
    }
}
