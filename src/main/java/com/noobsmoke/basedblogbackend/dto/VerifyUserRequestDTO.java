package com.noobsmoke.basedblogbackend.dto;

public record VerifyUserRequestDTO(
        String userName,
        String email,
        String verificationCode
) {
}
