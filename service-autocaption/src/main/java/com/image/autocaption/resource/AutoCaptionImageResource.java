package com.image.autocaption.resource;

import com.image.autocaption.exception.EmptyFileException;
import com.image.autocaption.exception.UnsupportedMediaTypeException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/api/images")
@CrossOrigin
public class AutoCaptionImageResource {

    private final ChatClient chatClient;

    private final Map<String, MimeType> supportedTypes;

    @Autowired
    public AutoCaptionImageResource(OllamaChatModel ollamaChatModel) {
        this.chatClient = ChatClient.create(ollamaChatModel);
        supportedTypes = Map.of("image/png", IMAGE_PNG,
                "image/jpeg", IMAGE_JPEG,
                "image/jpg", IMAGE_JPEG,
                "image/gif", IMAGE_GIF);
    }

    @PostMapping(value = "/captions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> generateCaption(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new EmptyFileException("Uploaded file is empty");
        }

        String contentType = imageFile.getContentType();
        if (contentType.isEmpty() || !supportedTypes.containsKey(contentType)) {
            throw new UnsupportedMediaTypeException("Unsupported file type: " + contentType);
        }

        UserMessage userMessage = UserMessage.builder()
                .text("""
                        Please generate exactly three different descriptive captions for this image.
                        Return them as a numbered list. Do not generate any other sentence.
                        Just a list of three captions.""")
                .media(new Media(supportedTypes.get(contentType), imageFile.getResource()))
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


