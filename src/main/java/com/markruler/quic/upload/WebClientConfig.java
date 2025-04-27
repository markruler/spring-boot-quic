package com.markruler.quic.upload;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient http3WebClient(WebClient.Builder builder) {
        HttpClient client =
                HttpClient.create()
                        .port(8080)
                        // Configure HTTP/3 protocol
                        .protocol(HttpProtocol.HTTP3)
                        // Configure HTTP/3 settings
                        .http3Settings(spec -> spec.idleTimeout(Duration.ofSeconds(5))
                                .maxData(10_000_000)
                                .maxStreamDataBidirectionalLocal(1_000_000));

        return builder.clientConnector(new ReactorClientHttpConnector(client)).build();
    }

}
