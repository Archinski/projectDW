package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ImageController(imageService)).build();
    }

    @Test
    public void testUploadImage() throws Exception {
        // Мокируем сервис для возврата ID изображения
        Mockito.when(imageService.uploadImage(Mockito.any(MultipartFile.class))).thenReturn("image-id");

        mockMvc.perform(multipart("/api/images/upload")
                        .file("file", new byte[]{1, 2, 3}))  // Мокируем файл
                .andExpect(status().isCreated())
                .andExpect(content().string("image-id"));
    }

    @Test
    public void testGetImage() throws Exception {
        // Мокируем возврат изображения
        Mockito.when(imageService.getImage(Mockito.anyString())).thenReturn(new byte[]{1, 2, 3});

        mockMvc.perform(get("/api/images/{imageId}", "image-id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/*"));
    }
}
