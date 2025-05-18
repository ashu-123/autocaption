package com.image.autocaption.resource;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin
public class AutoCaptionImageResource {

    private final ChatClient chatClient;

    @Autowired
    public AutoCaptionImageResource(OllamaChatModel ollamaChatModel) {
        this.chatClient = ChatClient.create(ollamaChatModel);
    }

    @PostMapping(value = "/generateCaption", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> generateCaption(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body(List.of("Error: No file uploaded."));
        }

        UserMessage userMessage = UserMessage.builder()
                .text("""
                        Please generate exactly three different descriptive captions for this image.
                        Return them as a numbered list. Do not generate any other sentence.
                        Just a list of three captions.""")
                .media(new Media(MimeTypeUtils.IMAGE_JPEG, imageFile.getResource()))
                .build();

        ChatResponse response = chatClient
                .prompt()
                .messages(userMessage)
                .call()
                .chatResponse();

        List<String> captions = List.of(response.getResult().getOutput().getText().split("\n"));

        return ResponseEntity.ok(captions);
    }

}


