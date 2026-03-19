package com.noobsmoke.basedblogbackend.service;

import com.noobsmoke.basedblogbackend.dto.LoginDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final FakeRepo repository;

    public void addUser(RegistrationDTO registrationDTO) {
        User newUser = new User(
                0,
                registrationDTO.firstName(),
                registrationDTO.lastName(),
                registrationDTO.userName(),
                registrationDTO.password(),
                LocalDate.now(),
                registrationDTO.avatar(),
                registrationDTO.favorite_topics()
        );
        repository.addUser(newUser);
    }

    public User findUser(LoginDTO loginDTO) {
        return repository.findUserByUserNameAndPassword(loginDTO.username(), loginDTO.password());
    }

    public User findUserByUsername(String userName) {
        return repository.findUserByUsername(userName);
    }

    public List<User> getAllUsers() {
        return repository.findAllUsers();
    }
}
