package br.com.amedigital.weather.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.Locale;

@SpringBootApplication
@EnableWebFlux
@EnableAutoConfiguration
public class ApplicationStarter {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("pt", "BR"));
        SpringApplication.run(ApplicationStarter.class, args);
    }


}
