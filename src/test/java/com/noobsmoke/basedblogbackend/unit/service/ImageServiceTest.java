package com.noobsmoke.basedblogbackend.unit.service;

import com.noobsmoke.basedblogbackend.TestUtils;
import com.noobsmoke.basedblogbackend.dto.ImageResponseDTO;
import com.noobsmoke.basedblogbackend.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest extends TestUtils {

    private ImageService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ImageService(
                mock(WebClient.class),
                testImageServiceBucketPrefix,
                testThumbnailServiceBucketPrefix
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "OsoInfinite",
            "AfrincaKing",
            "Supposani",
            "Dios"
    })
    void shouldBuildImageResponseFromKey(String username) {
        String fullImageKey = username + "_test_image.jpg";
        ImageResponseDTO expectedResponse = getImageResponse(username);
        ImageResponseDTO actualResponse = underTest.buildImageResponseFromKey(fullImageKey);
        assertEquals(expectedResponse.imageKey(), actualResponse.imageKey());
        assertEquals(expectedResponse.imageUrl(), actualResponse.imageUrl());
        assertEquals(expectedResponse.thumbnailUrl(), actualResponse.thumbnailUrl());
    }

}
