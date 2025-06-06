package com.image.autocaption.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.image.autocaption.constant.ImageTypes.getSupportedContentTypes;

@Service
public class AutoCaptionImageService {

    private final ChatClient chatClient;

    private static final Path IMAGES = Paths.get(System.getProperty("user.dir"), "images");

    private final CacheManager cacheManager;

    private final KeyGenerator keyGenerator;

    public AutoCaptionImageService(OllamaChatModel ollamaChatModel, CacheManager cacheManager, KeyGenerator keyGenerator) {
        this.chatClient = ChatClient.create(ollamaChatModel);
        this.cacheManager = cacheManager;
        this.keyGenerator = keyGenerator;
    }

    @Cacheable(value = "imageCache", keyGenerator = "cacheKeyGenerator")
    public List<String> createCaptions(byte[] image, MultipartFile imageFile) throws IOException {

        if (!Files.exists(IMAGES)) {
            Files.createDirectories(IMAGES);
        }

        String filename = Path.of(imageFile.getOriginalFilename()).getFileName().toString();
        Path destinationFile = IMAGES.resolve(filename);
        // Save the file
        Files.copy(imageFile.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);


        UserMessage userMessage = UserMessage.builder()
                .text("""
                        Please generate exactly three different descriptive captions for this image.
                        Return them as a numbered list. Do not generate any other sentence.
                        Just a list of three captions.""")
                .media(new Media(getSupportedContentTypes().get(imageFile.getContentType()), imageFile.getResource()))
                .build();

        ChatResponse response = chatClient
                .prompt()
                .messages(userMessage)
                .call()
                .chatResponse();

        return List.of(response.getResult().getOutput().getText().split("\n"));
    }

}
