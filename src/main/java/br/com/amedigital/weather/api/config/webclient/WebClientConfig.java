package br.com.amedigital.weather.api.config.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.channel.BootstrapHandlers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

import java.util.function.Function;

@Component
public class WebClientConfig {

    @Value("${client.timeout-seconds:}")
    private Integer clientTimeout;

    @Bean
    public WebClient webClient() {

        var webClient = WebClient.builder();
        return webClient
                .clientConnector(getHttpClient())
                .build();
    }

    private ClientHttpConnector getHttpClient() {

        HttpClient httpClient;

        if (clientTimeout == null) {
            httpClient = HttpClient.create(ConnectionProvider.newConnection()).tcpConfiguration(getClient());
        }else{
            httpClient = HttpClient.create(ConnectionProvider.newConnection()).tcpConfiguration(getClientTimeout()).keepAlive(true);
        }

        return new ReactorClientHttpConnector(httpClient);
    }

    private Function<TcpClient, TcpClient> getClientTimeout() {
        return client -> {
            var tcpClient = client.bootstrap(b -> BootstrapHandlers.updateLogSupport(b, new WebClientLogs()));
            return tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientTimeout * 100)
                    .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(clientTimeout)
                    ));
        };
    }

    private Function<TcpClient, TcpClient> getClient() {
        return client -> client.bootstrap(b -> BootstrapHandlers.updateLogSupport(b, new WebClientLogs()));
    }

}