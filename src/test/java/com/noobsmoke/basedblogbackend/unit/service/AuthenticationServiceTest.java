package com.noobsmoke.basedblogbackend.unit.service;

import com.noobsmoke.basedblogbackend.TestUtils;
import com.noobsmoke.basedblogbackend.dto.*;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import com.noobsmoke.basedblogbackend.service.AuthenticationService;
import com.noobsmoke.basedblogbackend.service.EmailVerificationService;
import com.noobsmoke.basedblogbackend.service.ImageService;
import com.noobsmoke.basedblogbackend.service.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private Authentication authentication;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private ImageService imageService;

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
        ImageResponseDTO imageResponseDTO = getImageResponse(userResponseDTO.userName());

        when(fakeRepo.containsUsername(registrationDTO.userName())).thenReturn(false);
        when(userMapper.toUserEntity(registrationDTO)).thenReturn(user);
        when(imageService.uploadImage(any(), any())).thenReturn(imageResponseDTO);
        when(passwordEncoder.encode(registrationDTO.password())).thenReturn(fakeEncodedPassword);
        when(jwtService.generateToken(user)).thenReturn(fakeToken);
        when(jwtService.getJwtExpirationTime()).thenReturn(fakeTime);
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDTO);
        when(emailVerificationService.buildVerificationEmailHtml(anyString())).thenReturn("<html>swamp</html>");

        AuthResponseDTO authResponseDTO = underTest.register(registrationDTO);

        assertEquals(userResponseDTO.userName(), authResponseDTO.userResponse().userName());
        assertEquals(userResponseDTO.firstName(), authResponseDTO.userResponse().firstName());
        assertEquals(userResponseDTO.lastName(), authResponseDTO.userResponse().lastName());
        assertEquals(fakeEncodedPassword, user.getPassword());
        assertEquals(fakeTime, authResponseDTO.expirationTime());
        assertEquals(fakeToken, authResponseDTO.jwtToken());
        assertEquals(imageResponseDTO.imageKey(), authResponseDTO.userImage().imageKey());
        assertEquals(imageResponseDTO.imageUrl(), authResponseDTO.userImage().imageUrl());
        assertEquals(imageResponseDTO.thumbnailUrl(), authResponseDTO.userImage().thumbnailUrl());

        verify(fakeRepo).containsUsername(registrationDTO.userName());
        verify(userMapper).toUserEntity(registrationDTO);
        verify(passwordEncoder).encode(registrationDTO.password());
        verify(fakeRepo).addUser(user);
        verify(jwtService).generateToken(user);
        verify(jwtService).getJwtExpirationTime();
        verify(userMapper).toUserResponse(user);
        verify(emailVerificationService).buildVerificationEmailHtml(anyString());
        verify(emailVerificationService).sendVerificationEmail(
                user.getEmail(),
                "[" + user.getUsername() + "] Young Based Blog Account Verification",
                "<html>swamp</html>"

        );
        verify(imageService).uploadImage(registrationDTO.userName(), registrationDTO.avatar());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUsername() {
        RegistrationDTO registrationDTO = getRegistrationDTOList().getFirst();
        when(fakeRepo.containsUsername(registrationDTO.userName())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.register(registrationDTO));
        assertEquals("Username Already Exists", exception.getMessage());
        verify(fakeRepo, never()).addUser(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "  ", ""})
    void shouldThrowExceptionWhenUserNameIsNullOrBlank(String username) {
        username = username.equals("null") ? null : username;
        RegistrationDTO registrationDTO = getCustomRegistrationDTO(
                "Osaretin",
                "Omofonmwan",
                username,
                "test",
                "test@email.com",
                null,
                null
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.register(registrationDTO));
        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void shouldLoginSuccessfully() {
        User user = getUsers().getFirst();
        String fakeToken = "Batman";
        long fakeExpirationTime = 2000L;
        LoginDTO loginDTO = new LoginDTO("OsoInfinite", "OsoInfinite");
        UserResponseDTO userResponseDTO = getExpectedResponseList().getFirst();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(fakeRepo.findUserByUsername(loginDTO.username())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(fakeToken);
        when(jwtService.getJwtExpirationTime()).thenReturn(fakeExpirationTime);
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDTO);

        AuthResponseDTO authResponseDTO = underTest.login(loginDTO);

        assertEquals(fakeToken, authResponseDTO.jwtToken());
        assertEquals(fakeExpirationTime, authResponseDTO.expirationTime());
        assertEquals(userResponseDTO.userName(), authResponseDTO.userResponse().userName());
        assertEquals(userResponseDTO.avatar(), authResponseDTO.userResponse().avatar());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(fakeRepo).findUserByUsername(loginDTO.username());
        verify(jwtService).generateToken(user);
        verify(jwtService).getJwtExpirationTime();
        verify(userMapper).toUserResponse(user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "  ", ""})
    void shouldThrowExceptionWhenUsernameIsNullOrBlank(String username) {
        username = username.equals("null") ? null : username;
        LoginDTO loginDTO = new LoginDTO(username, "Testing");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.login(loginDTO));
        assertEquals("Username is required", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", " ", ""})
    void shouldThrowExceptionWhenPasswordIsNullOrBlank(String password) {
        password = password.equals("null") ? null : password;
        LoginDTO loginDTO = new LoginDTO("OsoInfinite", password);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.login(loginDTO));
        assertEquals("Password is required", exception.getMessage());
    }
}
