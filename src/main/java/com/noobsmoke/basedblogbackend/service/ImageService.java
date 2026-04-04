package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.config.WebClientConfig;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
@AllArgsConstructor
public class ImageService {

    private final WebClient webClient;

    public String uploadImage(MultipartFile imageFile) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder
                .part("file", imageFile.getResource())
                .filename(Objects.requireNonNull(imageFile.getOriginalFilename()))
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(imageFile.getContentType())));

        String response = webClient.post()
                .uri("/upload")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(System.out::println)
                .block();

        JSONObject root = new JSONObject(response);
        return root.getString("url");
    }


}
