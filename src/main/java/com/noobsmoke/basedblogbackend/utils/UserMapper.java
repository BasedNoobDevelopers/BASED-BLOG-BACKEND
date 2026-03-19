package com.noobsmoke.basedblogbackend.utils;

import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toUserEntity(RegistrationDTO registrationDTO) {
        return new User.Builder()
                .userName(registrationDTO.userName())
                .password(registrationDTO.password())
                .firstName(registrationDTO.firstName())
                .lastName(registrationDTO.lastName())
                .createdAt(LocalDateTime.now())
                .favoriteTopics(registrationDTO.favorite_topics())
                .email(registrationDTO.email())
                .build();
    }

    public UserResponseDTO toUserResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getAvatar(),
                user.getFavoriteTopics()
        );
    }


}
