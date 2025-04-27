package com.markruler.quic.upload;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class QuicController {

    private final WebClient http3WebClient;

    @PostMapping(value = "/video/binary", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<String> uploadBinaryFile(
            @RequestHeader("filename") String filename,
            @RequestBody byte[] fileBytes
    ) {
        UUID key = UUID.randomUUID();
        // TODO: save fileBytes to disk
        return Mono.just(key.toString());
    }

    /**
     * @see <a href="https://github.com/violetagg/spring-webflux-http3">demo</a>
     */
    @GetMapping("/remote")
    Mono<String> remote() {
        return http3WebClient.get()
//                .uri("https://blog.cloudflare.com/")
                .uri("https://projectreactor.io/")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/string")
    Mono<String> string() {
        return Mono.just("remote");
    }

}
