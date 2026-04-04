package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.config.WebClientConfig;
import com.noobsmoke.basedblogbackend.dto.ImageServiceResponseDTO;
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
    private final String thumbnailPrefix = "https://thumbnail-image-service.s3.us-east-2.amazonaws.com/thumbs/";

    public String[] uploadImage(String username, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }
        try {
            String fileName = username + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder
                    .part("file", imageFile.getResource())
                    .filename(fileName)
                    .contentType(MediaType.parseMediaType(Objects.requireNonNull(imageFile.getContentType())));

            ImageServiceResponseDTO response = webClient.post()
                    .uri("/upload")
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(ImageServiceResponseDTO.class)
                    .block();

            if (response == null) {
                throw new IllegalStateException("Unable to receive response from service");
            }
            if (response.statusCode() >= 400 && response.statusCode() <= 499) {
                throw new IllegalArgumentException("Something wrong with image");
            }
            if (response.statusCode() >= 500 && response.statusCode() <= 599) {
                throw new IllegalStateException("Something off with image service");
            }
            return new String[]{response.url(), thumbnailPrefix+fileName};
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload image", e);
        }
    }
}
