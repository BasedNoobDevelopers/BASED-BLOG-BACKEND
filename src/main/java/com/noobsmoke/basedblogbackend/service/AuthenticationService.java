package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final FakeRepo fakeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public UserResponseDTO register(RegistrationDTO registrationDTO) {
        if (registrationDTO.userName() == null)
            throw new IllegalArgumentException("Username is required");
        if (fakeRepo.containsUsername(registrationDTO.userName()))
            throw new IllegalArgumentException("Username Already Exists");
        User user = userMapper.toUserEntity(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        fakeRepo.addUser(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponseDTO login(LoginDTO loginDTO) {
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
        return userMapper.toUserResponse(fakeRepo.findUserByUserNameAndPassword(loginDTO.username(), loginDTO.password()));
    }

    public List<UserResponseDTO> allUsers() {
        return fakeRepo.findAllUsers().stream().map(userMapper::toUserResponse).toList();
    }
}
