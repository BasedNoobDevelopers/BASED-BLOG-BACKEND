package com.noobsmoke.basedblogbackend.dto;

import java.util.List;

public record RegistrationDTO(
    String firstName,
    String lastName,
    String userName,
    String password,
    String email,
    String avatar,
    List<String> favorite_topics
) {
}
