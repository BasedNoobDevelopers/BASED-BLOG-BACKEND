package com.noobsmoke.basedblogbackend.dto;

public record AuthResponseDTO(
        String jwtToken,
        long expirationID,
        UserResponseDTO userResponse
) {
}
