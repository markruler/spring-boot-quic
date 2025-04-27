package com.markruler.quic.upload;

import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.http.Http3SslContextSpec;
import reactor.netty.http.HttpProtocol;

import java.time.Duration;

/**
 * Server-side HTTP/3 configuration.
 */
@Component
public class Http3NettyWebServerCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    private static final int ONE_MB = 1024 * 1024;

    @Override
    public void customize(NettyReactiveWebServerFactory factory) {
        factory.addServerCustomizers(server -> {
            SslBundle sslBundle = factory.getSslBundles().getBundle("server-http3");
            Http3SslContextSpec sslContextSpec =
                    Http3SslContextSpec.forServer(sslBundle.getManagers().getKeyManagerFactory(), sslBundle.getKey().getPassword());

            return server
                    .httpRequestDecoder(httpRequestDecoderSpec -> httpRequestDecoderSpec
                            .maxInitialLineLength(4 * ONE_MB)
                    )
                    // .port(port) // properties: server.port
                    // Configure HTTP/3 protocol
                    .protocol(HttpProtocol.HTTP3)
                    .idleTimeout(Duration.ofMinutes(5))
                    .readTimeout(Duration.ofMinutes(5))
                    .requestTimeout(Duration.ofMinutes(5))
                    // Configure HTTP/3 SslContext
                    .secure(spec -> spec.sslContext(sslContextSpec))
                    // Configure HTTP/3 settings
                    .http3Settings(spec -> spec
                            .idleTimeout(Duration.ofMinutes(5))
                            .maxData(10_000_000)
                            .maxStreamDataBidirectionalRemote(1_000_000)
                            .maxStreamsBidirectional(100));
        });
    }
}
