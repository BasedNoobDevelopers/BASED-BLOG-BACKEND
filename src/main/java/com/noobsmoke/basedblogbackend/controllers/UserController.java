package com.noobsmoke.basedblogbackend.controllers;

import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(loginDTO.username() + " Is Trying to Login");
    }

    @PostMapping("/register")
    public ResponseEntity<String>  register(@RequestBody RegistrationDTO registrationDTO) {
        return ResponseEntity.ok(registrationDTO.firstName() + " " + registrationDTO.lastName() + " Has Registered");
    }
}
