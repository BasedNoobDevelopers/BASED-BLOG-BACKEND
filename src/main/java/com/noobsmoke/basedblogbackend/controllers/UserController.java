package com.noobsmoke.basedblogbackend.controllers;

import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> authenticatedUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getMyInfo(authentication));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
