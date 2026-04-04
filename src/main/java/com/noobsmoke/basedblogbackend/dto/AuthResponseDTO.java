package com.noobsmoke.basedblogbackend.dto;

public record AuthResponseDTO(
        String jwtToken,
        long expirationTime,
        UserResponseDTO userResponse,
        ImageResponseDTO userImages
) {
}
