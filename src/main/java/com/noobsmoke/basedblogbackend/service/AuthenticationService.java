package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.*;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final FakeRepo fakeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JWTService jwtService;

    public AuthResponseDTO register(RegistrationDTO registrationDTO) {
        if (registrationDTO.userName() == null)
            throw new IllegalArgumentException("Username is required");
        if (fakeRepo.containsUsername(registrationDTO.userName()))
            throw new IllegalArgumentException("Username Already Exists");
        User user = userMapper.toUserEntity(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        user.setCreatedDate(LocalDateTime.now());
        fakeRepo.addUser(user);
        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token, jwtService.getJwtExpirationTime(), userMapper.toUserResponse(user));
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        if (loginDTO.username() == null)
            throw new IllegalArgumentException("Username is required");
        if (loginDTO.password() == null)
            throw new IllegalArgumentException("Password is required");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.username(),
                        loginDTO.password()
                )
        );

        User returningUser = fakeRepo.findUserByUsername(loginDTO.username());
        String token = jwtService.generateToken(returningUser);
        return new AuthResponseDTO(token, jwtService.getJwtExpirationTime(), userMapper.toUserResponse(returningUser));
    }

    public void verifyUser(VerifyUserRequestDTO verifyUserRequestDTO) {
        User user = fakeRepo.findUserByEmail(verifyUserRequestDTO.email());
        if (user.getVerificationExpirationAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification Code Has Expired");
        }
        if (!user.getVerificationCode().equals(verifyUserRequestDTO.verificationCode())) {
            throw new IllegalArgumentException("Incorrect Verification Code");
        }
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationExpirationAt(null);
        fakeRepo.addUser(user);
    }
}
