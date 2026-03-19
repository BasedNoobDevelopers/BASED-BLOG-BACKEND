package com.noobsmoke.basedblogbackend.controllers;

import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userService.findUser(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<String>  register(@RequestBody RegistrationDTO registrationDTO) {
        userService.addUser(registrationDTO);
        return ResponseEntity.ok(registrationDTO.firstName() + " " + registrationDTO.lastName() + " Has Registered");
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
