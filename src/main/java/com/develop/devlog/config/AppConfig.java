package com.develop.devlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "devlog")
public class AppConfig {

    private String jwtKey;

    public AppConfig(String jwtKey) {
        this.jwtKey = jwtKey;
    }

    public String getJwtKey() {
        return jwtKey;
    }
}