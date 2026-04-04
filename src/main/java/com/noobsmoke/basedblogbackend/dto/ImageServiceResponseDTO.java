package com.noobsmoke.basedblogbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageServiceResponseDTO(
        String message,
        @JsonProperty("presigned_url")
        String presigned_url,
        int statusCode,
        String url
) {
}
