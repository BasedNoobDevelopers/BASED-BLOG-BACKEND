package com.noobsmoke.basedblogbackend.unit.service;

import com.noobsmoke.basedblogbackend.dto.AuthResponseDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.mapper.UserMapper;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.repository.FakeRepo;
import com.noobsmoke.basedblogbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private FakeRepo repo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authentication;

    private UserService underTest;

    private LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        underTest = new UserService(repo, userMapper);
    }

    private User getUser() {
        return new User.Builder()
                .firstName("Osaretin")
                .lastName("Omofonmwan")
                .userName("OsoInfinite")
                .password("OsoInfinite")
                .email("OsoInfinite@test.com")
                .avatar("avatar.gif")
                .createdAt(localDateTime)
                .favoriteTopics(List.of("Manga", "TV", "BJJ"))
                .id(1L)
                .build();

    }

    @Test
    public void getMyInfoTest() {
        User testUser = getUser();
        UserResponseDTO expectedResponse = new UserResponseDTO(
                1L,
                "Osaretin",
                "Omofonmwan",
                "OsoInfinite",
                "avatar.gif",
                List.of("Manga", "TV", "BJJ")
        );
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userMapper.toUserResponse(testUser)).thenReturn(expectedResponse);
        UserResponseDTO authResponseDTO = underTest.getMyInfo(authentication);
        assertEquals("Osaretin", authResponseDTO.firstName());
    }

    @Test
    public void getMyInfoPrincipalNotUserTest() {
        when(authentication.getPrincipal()).thenReturn("Not A User");
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> underTest.getMyInfo(authentication));
        assertEquals("Invalid User Principal", exception.getMessage());
    }
}
