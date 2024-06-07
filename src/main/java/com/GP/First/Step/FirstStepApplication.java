package com.GP.First.Step;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class FirstStepApplication {

    @GetMapping("/test")
    public String message() {
        return "OK Finally ?";
    }

    public static void main(String[] args) {

        SpringApplication.run(FirstStepApplication.class, args);
    }

}