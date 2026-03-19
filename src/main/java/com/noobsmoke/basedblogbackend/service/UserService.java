package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final FakeRepo repository;
    private final UserMapper userMapper;

    public UserResponseDTO getMyInfo(Authentication authentication) {
        return userMapper.toUserResponse((User) authentication.getPrincipal());
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAllUsers()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}
