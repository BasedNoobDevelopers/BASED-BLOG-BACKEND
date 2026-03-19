package com.noobsmoke.basedblogbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    Long id;
    String firstName;
    String lastName;
    String userName;
    String avatar;
    List<String> favoriteTopics;
    String jwtToken;
    long expiresIn;
}
