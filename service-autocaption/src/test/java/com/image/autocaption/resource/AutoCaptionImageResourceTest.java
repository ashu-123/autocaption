package com.image.autocaption.resource;

import com.image.autocaption.config.GemmaTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AutoCaptionImageResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        GemmaTestContainer.createGemmaOllamaContainer();
    }

    @Test
    void shouldGenerateCaptions() throws Exception {

        ClassPathResource imageResource = new ClassPathResource("images/smiling baby.png");

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "smiling baby.png",
                MediaType.IMAGE_PNG_VALUE,
                imageResource.getInputStream()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images/captions")
                        .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("1. Pure joy!"))
                .andExpect(jsonPath("$[1]").value("2. The cutest giggle."))
                .andExpect(jsonPath("$[2]").value("3. Uncontainable happiness."));
    }

}