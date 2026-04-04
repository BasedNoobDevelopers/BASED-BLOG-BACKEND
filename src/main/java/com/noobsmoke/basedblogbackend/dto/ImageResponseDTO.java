package com.noobsmoke.basedblogbackend.dto;

public record ImageResponseDTO(
        String imageKey,
        String imageUrl,
        String thumbnailUrl
) {
}
