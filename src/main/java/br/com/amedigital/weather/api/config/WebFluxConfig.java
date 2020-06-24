package br.com.amedigital.weather.api.config;

import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.registerDefaults(false);
        configurer.customCodecs().decoder(new Jaxb2XmlDecoder());
        configurer.customCodecs().encoder(new Jaxb2XmlEncoder());

    }
}
