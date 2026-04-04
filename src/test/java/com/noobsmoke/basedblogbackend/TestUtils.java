package com.noobsmoke.basedblogbackend;

import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {

    private final LocalDateTime localDateTime = LocalDateTime.now();

    protected String fakeSecretKey = "FinalFlashdssafkdlaGSDsdfadnsdfasidoUyiOIoofdfakeu";
    protected long fakeExpirationTime = 2000L;

    protected RegistrationDTO getEmptyRegistrationDTO(String username) {
        return new RegistrationDTO(
                null,
                null,
                username,
                null,
                null,
                null,
                null
        );
    }

    protected List<RegistrationDTO> getRegistrationDTOList() {
        return List.of(
                new RegistrationDTO(
                        "Osaretin",
                        "Omofonmwan",
                        "OsoInfinite",
                        "OsoInfinite",
                        "OsoInfinite@test.com",
                        null,
                        List.of("BJJ", "Travel", "Martial Arts")
                ),
                new RegistrationDTO(
                        "Ajinboye",
                        "Uwensuyi",
                        "ondios",
                        "ondios",
                        "ondios@test.com",
                        null,
                        List.of("Boxing", "Travel", "Gaming")
                )
        );
    }

    protected List<User> getUsers() {
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

    protected List<UserResponseDTO> getExpectedResponseList() {
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
