package com.green.yp.subscriber;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Log4j2
public class GreenpagesSubscriberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenpagesSubscriberApplication.class, args);
        log.info("GreenpagesSubscriber Application started");
    }
}
