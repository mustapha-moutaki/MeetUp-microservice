package com.conferencehub.conference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ConferenceServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConferenceServiceApplication.class, args);
  }
}
