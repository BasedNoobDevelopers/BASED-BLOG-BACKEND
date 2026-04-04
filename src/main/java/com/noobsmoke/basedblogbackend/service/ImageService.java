package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.config.WebClientConfig;
import com.noobsmoke.basedblogbackend.dto.ImageResponseDTO;
import com.noobsmoke.basedblogbackend.dto.ImageServiceResponseDTO;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class ImageService {


    private final WebClient webClient;

    @Value("${image.service.thumbnail-service-bucket-prefix}")
    private String thumbnailServiceBucketPrefix;

    public ImageService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ImageResponseDTO uploadImage(String username, MultipartFile imageFile) {
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

            return new ImageResponseDTO(fileName, response.url(), thumbnailServiceBucketPrefix+fileName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload image", e);
        }
    }
}
