package com.noobsmoke.basedblogbackend.dto;

public record ImageServiceResponseDTO(
        String message,
        String presigned_url,
        int statusCode,
        String url
) {
}
