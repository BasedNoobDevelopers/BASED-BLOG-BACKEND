package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.*;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final FakeRepo fakeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JWTService jwtService;
    private final EmailVerificationService emailVerificationService;
    ;

    public AuthResponseDTO register(RegistrationDTO registrationDTO) {
        if (registrationDTO.userName() == null)
            throw new IllegalArgumentException("Username is required");
        if (fakeRepo.containsUsername(registrationDTO.userName()))
            throw new IllegalArgumentException("Username Already Exists");
        User user = userMapper.toUserEntity(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        user.setCreatedDate(LocalDateTime.now());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpirationAt(LocalDateTime.now().plusMinutes(15));
        fakeRepo.addUser(user);
        sendVerificationEmail(user.getUsername(), user.getEmail(), user.getVerificationCode());
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

    public void sendVerificationEmail(String userName, String email, String verificationCode) {
       String verificationImageUrl = "https://a.pinatafarm.com/428x336/88c63a438c/old-lady-at-computer.jpg";
        String subject = "Young Based Blog Account Verification: (" + userName + ")";
        String htmlMessage ="<!doctype html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\" />"
                + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\" />"
                + "</head>"
                + "<body style=\"margin:0;padding:0;background-color:#f4f6f8;\">"

                + "<div style=\"display:none;max-height:0;overflow:hidden;opacity:0;color:transparent;\">"
                + "Your verification code is " + verificationCode + ". It expires in 10 minutes."
                + "</div>"

                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#f4f6f8;\">"
                + "<tr><td align=\"center\" style=\"padding:24px 12px;\">"

                + "<table role=\"presentation\" width=\"600\" cellpadding=\"0\" cellspacing=\"0\" "
                + "style=\"width:600px;max-width:600px;background:#ffffff;border-radius:14px;overflow:hidden;\">"

                + "<tr><td style=\"padding:22px 24px;background:#0b5fff;\">"
                + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:18px;line-height:1.2;color:#ffffff;font-weight:700;\">YourApp</div>"
                + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:13px;line-height:1.4;color:#dbe7ff;margin-top:6px;\">Email verification</div>"
                + "</td></tr>"

                + "<tr><td>"
                + "<img src=\"" + verificationImageUrl + "\" "
                + "alt=\"Verification image\" "
                + "style=\"width:100%;max-width:600px;display:block;border:0;\" />"
                + "</td></tr>"

                + "<tr><td style=\"padding:28px 24px 10px 24px;\">"
                + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:16px;line-height:1.5;color:#111827;font-weight:700;\">Verify your email address</div>"
                + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:14px;line-height:1.6;color:#374151;margin-top:10px;\">Enter the code below to continue.</div>"

                + "<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:18px 0 6px 0;\">"
                + "<tr><td style=\"background:#f3f4f6;border:1px solid #e5e7eb;border-radius:12px;padding:14px 18px;\">"
                + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:28px;letter-spacing:6px;color:#111827;font-weight:800;text-align:center;\">"
                + verificationCode
                + "</div>"
                + "</td></tr></table>"

                + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.6;color:#6b7280;margin-top:6px;\">If you didn’t request this, ignore this email.</div>"
                + "</td></tr>"

                + "</table>"
                + "</td></tr></table>"

                + "</body></html>";
        System.out.println(email);
        emailVerificationService.sendVerificationEmail(email, subject, htmlMessage);

    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(90000) + 10000;
        return String.valueOf(code);
    }
}
