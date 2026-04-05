package com.noobsmoke.basedblogbackend.unit.service;

import com.noobsmoke.basedblogbackend.TestUtils;
import com.noobsmoke.basedblogbackend.dto.ImageResponseDTO;
import com.noobsmoke.basedblogbackend.dto.ImageServiceResponseDTO;
import com.noobsmoke.basedblogbackend.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ImageServiceTest extends TestUtils {

    private ImageService underTest;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    private WebClient.ResponseSpec responseSpec;
    private MultipartBodyBuilder multipartBodyBuilder;
    private MultipartBodyBuilder.PartBuilder partBuilder;


    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        multipartBodyBuilder = mock(MultipartBodyBuilder.class);
        partBuilder = mock(MultipartBodyBuilder.PartBuilder.class);

        underTest = new ImageService(
                webClient,
                testImageServiceBucketPrefix,
                testThumbnailServiceBucketPrefix
        );
    }

    @Test
//    @ValueSource(strings = {
//            "image/png"
////            "image/jpeg",
////            "image/jpg",
////            "image/gif"
//    })
    void shouldUploadImage() {
        String username = "OsoInfinite";
        ImageServiceResponseDTO imageServiceResponseDTO = getImageServiceResponse(username);
        ImageResponseDTO imageResponseDTO = getImageResponse(username);

        MultipartFile file = mock(MultipartFile.class);

        ByteArrayResource resource = new ByteArrayResource("test".getBytes()) {
            @Override
            public String getFilename() {
                return "test_image.jpg";
            }
        };

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpg");
        when(file.getOriginalFilename()).thenReturn("test_image.jpg");
        when(file.getResource()).thenReturn(resource);


        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/upload")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ImageServiceResponseDTO.class))
                .thenReturn(Mono.just(imageServiceResponseDTO));

        ImageResponseDTO actualResponse = underTest.uploadImage(username, file);

        assertEquals(imageResponseDTO.imageKey(), actualResponse.imageKey());
        assertEquals(imageResponseDTO.imageUrl(), actualResponse.imageUrl());
        assertEquals(imageResponseDTO.thumbnailUrl(), actualResponse.thumbnailUrl());


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

    @ParameterizedTest
    @ValueSource(strings = {
            " ",
            "",
            "null"
    })
    void shouldReturnNullIfKeyIsNullOrBlank(String imageKey) {
        imageKey = (imageKey.equals("null")) ? null : imageKey;
        assertNull(underTest.buildImageResponseFromKey(imageKey));
    }
}
