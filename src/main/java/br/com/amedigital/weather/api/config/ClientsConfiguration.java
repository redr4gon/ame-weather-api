package br.com.amedigital.weather.api.config;

import br.com.amedigital.weather.api.model.mail.MailConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

@Configuration
public class ClientsConfiguration {

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public MailConfig mailConfig(@Value("${ame.email.host}") final String host,
                                 @Value("${ame.email.username}") final String username,
                                 @Value("${ame.email.password}") final String password,
                                 @Value("${ame.email.sender}") final String sender,
                                 @Value("${ame.email.port}") final Integer port) {
        return new MailConfig(host, username, password, sender, port);
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public RetryableConfig retryableConfig(@Value("${retryable.max-retries}") Integer maxRetries,
                                           @Value("${retryable.min-duration}") Integer minDuration,
                                           @Value("${retryable.max-duration}") Integer maxDuration) {
        return new RetryableConfig(maxRetries, minDuration, maxDuration);
    }

    @Bean
    public DataBufferFactory dataBufferFactory() {
        return new DefaultDataBufferFactory();
    }
}
