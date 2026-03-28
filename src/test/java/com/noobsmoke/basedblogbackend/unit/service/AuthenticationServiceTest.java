package com.noobsmoke.basedblogbackend.unit.service;

import com.noobsmoke.basedblogbackend.TestUtils;
import com.noobsmoke.basedblogbackend.dto.AuthResponseDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import com.noobsmoke.basedblogbackend.service.AuthenticationService;
import com.noobsmoke.basedblogbackend.service.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest extends TestUtils {

    @Mock
    private FakeRepo fakeRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthenticationService underTest;

    @Test
    void shouldRegisterNewUser() {
        String fakeEncodedPassword = "3423423dadasd";
        String fakeToken = "Spiderman";
        long fakeTime = 2000L;
        RegistrationDTO registrationDTO = getRegistrationDTOList().getFirst();
        User user = getUsers().getFirst();
        UserResponseDTO userResponseDTO = getExpectedResponseList().getFirst();

        when(fakeRepo.containsUsername(registrationDTO.userName())).thenReturn(false);
        when(userMapper.toUserEntity(registrationDTO)).thenReturn(user);
        when(passwordEncoder.encode(registrationDTO.password())).thenReturn(fakeEncodedPassword);
        user.setCreatedDate(LocalDateTime.of(LocalDate.of(2026, 3, 26), LocalTime.of(15, 30)));
        when(jwtService.generateToken(user)).thenReturn(fakeToken);
        when(jwtService.getJwtExpirationTime()).thenReturn(fakeTime);
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDTO);

        AuthResponseDTO authResponseDTO = underTest.register(registrationDTO);

        assertEquals(userResponseDTO.userName(), authResponseDTO.userResponse().userName());
        assertEquals(userResponseDTO.firstName(), authResponseDTO.userResponse().firstName());
        assertEquals(userResponseDTO.lastName(), authResponseDTO.userResponse().lastName());
        assertEquals(fakeTime, authResponseDTO.expirationTime());
        assertEquals(fakeToken, authResponseDTO.jwtToken());

        verify(fakeRepo).containsUsername(registrationDTO.userName());
        verify(userMapper).toUserEntity(registrationDTO);
        verify(passwordEncoder).encode(registrationDTO.password());
        verify(fakeRepo).addUser(user);
        verify(jwtService).generateToken(user);
        verify(jwtService).getJwtExpirationTime();
        verify(userMapper).toUserResponse(user);
    }

}
