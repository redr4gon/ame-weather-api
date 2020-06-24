package br.com.amedigital.weather.api.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebFilter;

@Component
public class WebFilterConfig {

    private static final Logger LOG = LoggerFactory.getLogger(WebFilterConfig.class);

    @Value("${ame.context-path}")
    private String context;

    @Bean
    public WebFilter contextPathWebFilter() {
        return (exchange, chain) -> {
            LOG.debug("==== ContextPathFilter --> Checking context path! ==== ");

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // if do not start with the context we block it
            if (!path.startsWith(context)) {
                LOG.error("==== Path [{}] does not have the context path [{}] ====", path, context);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            LOG.debug("==== ContextPathFilter --> Path {} is OK ====", path);
            return chain.filter(
                    exchange.mutate()
                            .request(request.mutate().contextPath(context).build())
                            .build());
        };
    }
}
