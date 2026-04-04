package com.noobsmoke.basedblogbackend.dto;

public record VerifyUserRequestDTO(
        String email,
        String verificationCode
) {
}
