package com.noobsmoke.basedblogbackend.unit.service;

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

    @Test
    public void shouldReturnAuthenticatedUserInfo() {
        User testUser = getUsers().getFirst();
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
        assertEquals("Omofonmwan", authResponseDTO.lastName());
        assertEquals("OsoInfinite", authResponseDTO.userName());
        assertEquals("avatar.gif", authResponseDTO.avatar());
    }

    @Test
    public void shouldThrowWhenPrincipalIsNotUser() {
        when(authentication.getPrincipal()).thenReturn("Not A User");
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> underTest.getMyInfo(authentication));
        assertEquals("Invalid User Principal", exception.getMessage());
    }

    @Test
    public void shouldReturnAllUsers() {
        List<User> userList = getUsers();
        List<UserResponseDTO> userResponseDTOList = getExpectedResponseList();
        when(repo.findAllUsers()).thenReturn(userList);
        when(userMapper.toUserResponse(userList.getFirst())).thenReturn(userResponseDTOList.getFirst());
        when(userMapper.toUserResponse(userList.getLast())).thenReturn(userResponseDTOList.getLast());
        List<UserResponseDTO> actualResponse = underTest.getAllUsers();
        assertEquals("OsoInfinite", actualResponse.getFirst().userName());
        assertEquals("ondios", actualResponse.getLast().userName());
        assertEquals("Osaretin", actualResponse.getFirst().firstName());
        assertEquals("Ajinboye", actualResponse.getLast().firstName());
        assertEquals(2, actualResponse.size());

        verify(repo).findAllUsers();
        verify(userMapper).toUserResponse(userList.getFirst());
        verify(userMapper).toUserResponse(userList.getLast());


    }

    private List<User> getUsers() {
        return List.of(
                new User.Builder()
                        .firstName("Osaretin")
                        .lastName("Omofonmwan")
                        .userName("OsoInfinite")
                        .password("OsoInfinite")
                        .email("OsoInfinite@test.com")
                        .avatar("avatar.gif")
                        .createdAt(localDateTime)
                        .favoriteTopics(List.of("Manga", "TV", "BJJ"))
                        .id(1L)
                        .build(),
                new User.Builder()
                        .firstName("Ajinboye")
                        .lastName("Uwensuyi")
                        .userName("ondios")
                        .password("ondios")
                        .email("ondios@test.com")
                        .avatar("avatar.gif")
                        .createdAt(localDateTime)
                        .favoriteTopics(List.of("Manga", "Anime", "Boxing"))
                        .id(2L)
                        .build()
                );

    }

    private List<UserResponseDTO> getExpectedResponseList() {
        return List.of(
                new UserResponseDTO(
                        1L,
                        "Osaretin",
                        "Omofonmwan",
                        "OsoInfinite",
                        "avatar.gif",
                        List.of("Manga", "TV", "BJJ")
                ),
                new UserResponseDTO(
                        2L,
                        "Ajinboye",
                        "Uwensuyi",
                        "ondios",
                        "avatar.gif",
                        List.of("Manga", "Anime", "Boxing")
                )
        );
    }
}
