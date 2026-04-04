package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.ImageResponseDTO;
import com.noobsmoke.basedblogbackend.dto.ImageServiceResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class ImageService {


    private final WebClient webClient;
    private final String imageServiceBucketPrefix;
    private final String thumbnailServiceBucketPrefix;

    public ImageService(
            WebClient webClient,
            @Value("${image.service.image-service-bucket-prefix}")
            String imageServiceBucketPrefix,
            @Value("${image.service.thumbnail-service-bucket-prefix}")
            String thumbnailServiceBucketPrefix) {
        this.webClient = webClient;
        this.imageServiceBucketPrefix = imageServiceBucketPrefix;
        this.thumbnailServiceBucketPrefix = thumbnailServiceBucketPrefix;
    }

    public ImageResponseDTO uploadImage(String username, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }
        String contentType = imageFile.getContentType();
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("Image content type is required");
        }

        if (!contentType.equals("image/png")
            && !contentType.equals("image/jpeg")
            && !contentType.equals("image/jpg")
            && !contentType.equals("image/gif")
        ) {
            throw new IllegalArgumentException("Unsupported image type");
        }

        try {
            String originalFileName = Objects.requireNonNull(imageFile.getOriginalFilename());
            String fileName = buildFileName(username, originalFileName);

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder
                    .part("file", imageFile.getResource())
                    .filename(fileName)
                    .contentType(MediaType.parseMediaType(contentType));

            ImageServiceResponseDTO response = webClient.post()
                    .uri("/upload")
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(ImageServiceResponseDTO.class)
                    .block();

            if (response == null || response.url() == null || response.url().isBlank()) {
                throw new IllegalStateException("Unable to receive response from service");
            }

            return new ImageResponseDTO(fileName, response.url(), thumbnailServiceBucketPrefix+fileName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload image", e);
        }
    }

    public ImageResponseDTO buildImageResponseFromKey(String imageKey) {
        if (imageKey == null || imageKey.isBlank()) {
            return null;
        }

        return new ImageResponseDTO(
                imageKey,
                imageServiceBucketPrefix + imageKey,
                thumbnailServiceBucketPrefix + imageKey
        );
    }


    private String buildFileName(String username, String originalFileName) {
        String cleanedUpName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        return username + "_" + cleanedUpName;
    }
}
