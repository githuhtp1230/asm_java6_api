package com.example.asm_java6_api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "variables")
public class AppVariablesConfig {
    private CartConfig cartConfig = new CartConfig();

    @Getter
    @Setter
    public class CartConfig {
        private String keyPrefixCart;
        private String keySuffixOrder;
    }
}