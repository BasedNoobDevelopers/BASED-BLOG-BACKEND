package com.noobsmoke.basedblogbackend.dto;

import java.util.List;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String userName,
        String avatar,
        List<String> favoriteTopics
) {
}
