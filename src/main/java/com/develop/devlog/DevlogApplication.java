package com.develop.devlog;

import com.develop.devlog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class DevlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevlogApplication.class, args);
    }

}
