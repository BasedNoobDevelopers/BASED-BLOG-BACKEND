package com.noobsmoke.basedblogbackend.controllers;

import com.noobsmoke.basedblogbackend.dto.AuthResponseDTO;
import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody
            @Valid
            @NotNull
            LoginDTO loginDTO) {
        log.info(loginDTO.username() + " Is Trying to Login!");
        return ResponseEntity.ok(authenticationService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO>  register(
            @RequestBody
            @Valid
            @NotNull
            RegistrationDTO registrationDTO) {
      return ResponseEntity.ok(authenticationService.register(registrationDTO));
    }
}
