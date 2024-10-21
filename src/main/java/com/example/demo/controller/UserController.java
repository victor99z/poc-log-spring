package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.model.User;
import com.google.gson.Gson;
import io.prometheus.metrics.config.PrometheusProperties;
import io.prometheus.metrics.core.metrics.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {

        var fodase = Counter.builder(PrometheusProperties.get())
                .name("count-hello-endpoint")
                .build();

        fodase.inc();

        log.info("/hello");

        if(name.contains("a")){
            log.error("/hello error");
        }

        return new Gson().toJson(
                new User()
                        .setName("Victor")
                        .setCpf("11342323")
                        .setId(10L)
                );
    }
}
