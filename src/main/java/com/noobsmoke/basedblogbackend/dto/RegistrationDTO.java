package com.noobsmoke.basedblogbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record RegistrationDTO(
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @NotBlank
    String userName,
    @NotBlank
    String password,
    @Email
    String email,
    MultipartFile avatar,
    List<String> favoriteTopics
) {
}
