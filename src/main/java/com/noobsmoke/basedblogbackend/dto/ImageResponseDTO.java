package com.noobsmoke.basedblogbackend.dto;

public record ImageResponseDTO(
        String imageKey,
        String imageURL,
        String thumbnailURL
) {
}
