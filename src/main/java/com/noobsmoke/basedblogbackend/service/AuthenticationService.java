package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.*;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthenticationService {

    private final FakeRepo fakeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JWTService jwtService;
    private final EmailVerificationService emailVerificationService;
    private final ImageService imageService;

    public AuthenticationService(
            FakeRepo fakeRepo,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserMapper userMapper,
            JWTService jwtService,
            EmailVerificationService emailVerificationService,
            ImageService imageService
    ) {
        this.fakeRepo = fakeRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.emailVerificationService = emailVerificationService;
        this.imageService = imageService;
    }

    public AuthResponseDTO register(RegistrationDTO registrationDTO) {
       validateRegistrationRequest(registrationDTO);
        if (fakeRepo.containsUsername(registrationDTO.userName())) {
            throw new IllegalArgumentException("Username Already Exists");
        }

        User user = userMapper.toUserEntity(registrationDTO);

        ImageResponseDTO imageResponseDTO = imageService.uploadImage(user.getUsername(), registrationDTO.avatar());

        user.setAvatar(imageResponseDTO.imageKey());
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        user.setCreatedDate(LocalDateTime.now());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpirationAt(LocalDateTime.now().plusMinutes(10));

        fakeRepo.addUser(user);

        sendVerificationCodeEmail(user.getUsername(), user.getEmail(), user.getVerificationCode());
        String token = jwtService.generateToken(user);
        UserResponseDTO userResponseDTO = userMapper.toUserResponse(user);

        return new AuthResponseDTO(token, jwtService.getJwtExpirationTime(), userResponseDTO, imageResponseDTO);
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        validateLoginRequest(loginDTO);

        try {
           authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.username(),
                            loginDTO.password()
                    )
            );
        } catch (DisabledException e) {
            throw new IllegalArgumentException(loginDTO.username() + " Is Not Enabled!");
        }

        User returningUser = fakeRepo.findUserByUsername(loginDTO.username());
        String token = jwtService.generateToken(returningUser);


        ImageResponseDTO imageResponseDTO = buildImageResponse(returningUser.getAvatar());
        UserResponseDTO userResponseDTO = userMapper.toUserResponse(returningUser);

        return new AuthResponseDTO(token, jwtService.getJwtExpirationTime(), userResponseDTO, imageResponseDTO);
    }

    public void verifyUser(VerifyUserRequestDTO verifyUserRequestDTO) {
        if (verifyUserRequestDTO == null) {
            throw new IllegalArgumentException("Verification Request Issues");
        }

        String verificationEmail = verifyUserRequestDTO.email();
        String verificationCode = verifyUserRequestDTO.verificationCode();

        if (verificationEmail == null || verificationEmail.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (verificationCode == null || verificationCode.isBlank()) {
            throw new IllegalArgumentException("Verification code is required");
        }

        User user = fakeRepo.findUserByEmail(verificationEmail);

        if (user.isEnabled()) {
            throw new IllegalArgumentException(verificationEmail + " Is Already Verified");
        }
        if (user.getVerificationCode() == null || user.getVerificationExpirationAt() == null) {
            throw new IllegalStateException("No active verification request exists for this user");
        }
        if (user.getVerificationExpirationAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification Code Is Expired");
        }
        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new IllegalArgumentException("Verification Code Is Incorrect");
        }
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationExpirationAt(null);
        fakeRepo.updateExistingUser(user);
    }

    public void resendVerification(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Please Enter Email");
        }
        User user = fakeRepo.findUserByEmail(email);

        if (user.isEnabled()) {
            throw new IllegalStateException(email + " Is Already Verified");
        }

        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationExpirationAt(LocalDateTime.now().plusMinutes(10));
        sendVerificationCodeEmail(user.getUsername(), email, verificationCode);
        fakeRepo.updateExistingUser(user);
    }

    // Helper Methods (Start)

    private ImageResponseDTO buildImageResponse(String avatarKey) {
        if (avatarKey == null || avatarKey.isBlank()) {
            return null;
        }

        return imageService.buildImageResponseFromKey(avatarKey);
    }

    private void validateRegistrationRequest(RegistrationDTO registrationDTO) {
        if (registrationDTO == null) {
            throw new IllegalArgumentException("Registration is required");
        }

        if (registrationDTO.firstName() == null || registrationDTO.firstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (registrationDTO.lastName() == null || registrationDTO.lastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }

        if (registrationDTO.userName() == null || registrationDTO.userName().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (registrationDTO.password() == null || registrationDTO.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (registrationDTO.email() == null || registrationDTO.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
    }

    private void validateLoginRequest(LoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new IllegalArgumentException("Login request is required");
        }

        if (loginDTO.username() == null || loginDTO.username().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (loginDTO.password() == null || loginDTO.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    private void sendVerificationCodeEmail(String username, String email, String verificationCode) {
        String subject = "[" + username + "] Young Based Blog Account Verification";
        String htmlMessage = emailVerificationService.buildVerificationEmailHtml(verificationCode);
        emailVerificationService.sendVerificationEmail(email, subject, htmlMessage);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int verificationCode = random.nextInt(90000) + 10000;
        return String.valueOf(verificationCode);
    }

    // Helper Methods (End)
}
