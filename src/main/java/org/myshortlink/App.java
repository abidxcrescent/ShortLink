package org.myshortlink;


import org.myshortlink.Service.ShortURLService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
@Bean
public ShortURLService shortURL() {
    return new ShortURLService(10);
}
}

