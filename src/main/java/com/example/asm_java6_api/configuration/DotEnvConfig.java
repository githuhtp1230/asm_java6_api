package com.example.asm_java6_api.configuration;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@Configuration
public class DotEnvConfig {
    @PostConstruct
    public void loadDotEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .load();

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
}
