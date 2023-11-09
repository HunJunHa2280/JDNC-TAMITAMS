package com.example.jdnctamitams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JdncTamitamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdncTamitamsApplication.class, args);
    }

}
