package com.study.orderservice.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/health-check")
@RequiredArgsConstructor
public class HealthCheckController {

    private final Environment env;

    @GetMapping
    public String check() {
        return String.format("It's Working in Order Service"
                + ", port(server.port)=" + env.getProperty("server.port"));
    }
}
