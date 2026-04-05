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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ImageServiceTest extends TestUtils {

    private ImageService underTest;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;
    private MultipartFile multipartFile;


    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        multipartFile = mock(MultipartFile.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);


        underTest = new ImageService(
                webClient,
                testImageServiceBucketPrefix,
                testThumbnailServiceBucketPrefix
        );
    }

    @Test
    void shouldUploadImage() {
        String username = "OsoInfinite";
        ImageServiceResponseDTO imageServiceResponseDTO = getImageServiceResponse(username);
        ImageResponseDTO imageResponseDTO = getImageResponse(username);

        ByteArrayResource resource = new ByteArrayResource("test".getBytes()) {
            @Override
            public String getFilename() {
                return "test_image.jpg";
            }
        };

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("image/jpg");
        when(multipartFile.getOriginalFilename()).thenReturn("test_image.jpg");
        when(multipartFile.getResource()).thenReturn(resource);


        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/upload")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ImageServiceResponseDTO.class))
                .thenReturn(Mono.just(imageServiceResponseDTO));

        ImageResponseDTO actualResponse = underTest.uploadImage(username, multipartFile);

        assertEquals(imageResponseDTO.imageKey(), actualResponse.imageKey());
        assertEquals(imageResponseDTO.imageUrl(), actualResponse.imageUrl());
        assertEquals(imageResponseDTO.thumbnailUrl(), actualResponse.thumbnailUrl());
    }

    @Test
    void shouldThrowExceptionWhenResponseIsNull() {
        String username = "OsoInfinite";
        ImageServiceResponseDTO imageServiceResponseDTO = getCustomImageServiceResponse(
                "Test",
                "Test",
                200,
                ""
        );

        ByteArrayResource resource = new ByteArrayResource("test".getBytes()) {
            @Override
            public String getFilename() {
                return "test_image.jpg";
            }
        };

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("image/jpg");
        when(multipartFile.getOriginalFilename()).thenReturn("test_image.jpg");
        when(multipartFile.getResource()).thenReturn(resource);


        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/upload")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ImageServiceResponseDTO.class))
                .thenReturn(Mono.just(imageServiceResponseDTO));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> underTest.uploadImage(username, multipartFile));

        assertEquals("Unable to receive response from service", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenFileFailedToUpload() {
        String username = "OsoInfinite";
        ImageServiceResponseDTO imageServiceResponseDTO = getImageServiceResponse(username);


        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("image/jpg");
        when(multipartFile.getOriginalFilename()).thenReturn("test_image.jpg");



        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/upload")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ImageServiceResponseDTO.class))
                .thenReturn(Mono.just(imageServiceResponseDTO));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.uploadImage(username, multipartFile));

        assertEquals("'part' must not be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", ""})
    void shouldThrowExceptionWhenImageFileIsNullOrEmpty(String scenario) {
        if (scenario.isBlank()) {
            when(multipartFile.isEmpty()).thenReturn(true);
        } else {
            multipartFile = null;
        }

        MultipartFile finalMultipartFile = multipartFile;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.uploadImage("Test", finalMultipartFile));
        assertEquals("Image file is required", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "null",
            "",
            " "
    })
    void shouldThrowExceptionWhenImageFileContentTypeIsNullOrBlank(String contentType) {
        contentType = (contentType.equals("null")) ? null : contentType;
        when(multipartFile.getContentType()).thenReturn(contentType);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.uploadImage("Test", multipartFile));
        assertEquals("Image content type is required", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "image/nef",
            "image/webp"
    })
    void shouldThrowExceptionImageContentTypeIsNotSupported(String contentType) {
        when(multipartFile.getContentType()).thenReturn(contentType);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.uploadImage("test", multipartFile));
        assertEquals("Unsupported image type", exception.getMessage());
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
