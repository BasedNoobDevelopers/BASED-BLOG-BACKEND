package com.noobsmoke.basedblogbackend.controllers;

import com.noobsmoke.basedblogbackend.dto.AuthResponseDTO;
import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.VerifyUserRequestDTO;
import com.noobsmoke.basedblogbackend.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody
            @Valid
            @NotNull
            LoginDTO loginDTO) {
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

    @PostMapping
    public ResponseEntity<?> verifyUser(
            @RequestBody
            @Valid
            @NotNull
            VerifyUserRequestDTO verifyUserRequestDTO
    ) {
        try {
            authenticationService.verifyUser(verifyUserRequestDTO);
            return ResponseEntity.ok(verifyUserRequestDTO.email() + " Has Been Successfully Verified");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
