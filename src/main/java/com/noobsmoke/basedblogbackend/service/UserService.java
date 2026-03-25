package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final FakeRepo repository;
    private final UserMapper userMapper;

    public UserResponseDTO getMyInfo(Authentication authentication) {
        log.info("UserService --- getMyInfo --- Running getMyInfo method");
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            log.error("UserService --- getMyInfo --- Something went wrong with the getMyInfo method");
            throw new IllegalStateException("Invalid User Principal");
        }
        log.info("UserService --- getMyInfo --- " + user.getUsername());
        return userMapper.toUserResponse(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAllUsers()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}
