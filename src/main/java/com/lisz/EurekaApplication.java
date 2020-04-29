package com.lisz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// 集群启动是要配置program arguments：--spring.profiles.active=7901/7902/7903
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main( String[] args ) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
