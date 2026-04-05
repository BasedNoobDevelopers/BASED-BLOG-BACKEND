package com.noobsmoke.basedblogbackend;

import com.noobsmoke.basedblogbackend.dto.ImageResponseDTO;
import com.noobsmoke.basedblogbackend.dto.ImageServiceResponseDTO;
import com.noobsmoke.basedblogbackend.dto.RegistrationDTO;
import com.noobsmoke.basedblogbackend.dto.UserResponseDTO;
import com.noobsmoke.basedblogbackend.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TestUtils {

    private final LocalDateTime localDateTime = LocalDateTime.now();

    protected String fakeSecretKey = "FinalFlashdssafkdlaGSDsdfadnsdfasidoUyiOIoofdfakeu";
    protected long fakeExpirationTime = 2000L;

    protected ImageServiceResponseDTO getImageServiceResponse(String username) {
        return new ImageServiceResponseDTO(
                "Image uploaded",
                username + "_test_image.jpg",
                200,
                "http://test-domain/" + username + "_test_image.jpg"
        );
    }

    protected ImageResponseDTO getImageResponse(String username) {
        return new ImageResponseDTO(
                username + "_test_image.jpg",
                "http://test-domain/" + username + "_test_image.jpg",
                "http://test-domain-thumbnail/" + username + "_test_image.jpg"
        );
    }

    protected RegistrationDTO getCustomRegistrationDTO(
            String firstName,
            String lastName,
            String username,
            String password,
            String email,
            MultipartFile avatar,
            List<String> favoriteTopics
    ) {
        return new RegistrationDTO(
                firstName,
                lastName,
                username,
                password,
                email,
                avatar,
                favoriteTopics
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
