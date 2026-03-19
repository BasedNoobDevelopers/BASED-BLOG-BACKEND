package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import com.noobsmoke.basedblogbackend.utils.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final FakeRepo repository;
    private final UserMapper userMapper;

    public void addUser(RegistrationDTO registrationDTO) {
        if (registrationDTO.userName() == null)
            throw new IllegalArgumentException("Username is Mandatory");
        if (repository.containsUsername(registrationDTO.userName()))
            throw new IllegalArgumentException(("Username Already Existed"));
        User newUser = userMapper.toUserEntity(registrationDTO);
        repository.addUser(newUser);
    }

    public UserResponseDTO findUser(LoginDTO loginDTO) {
        if (loginDTO.username() == null)
            throw new IllegalArgumentException("Username is Mandatory");
        if (loginDTO.password() == null)
            throw new IllegalArgumentException("Password is Mandatory");
        return userMapper.toUserResponse(repository.findUserByUserNameAndPassword(loginDTO.username(), loginDTO.password()));
    }

    public UserResponseDTO findUserByUsername(String userName) {
        if (userName == null)
            throw new IllegalArgumentException("Username is Mandatory");
        return userMapper.toUserResponse(repository.findUserByUsername(userName));
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAllUsers()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}
