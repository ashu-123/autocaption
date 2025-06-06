package com.image.autocaption.resource;

import com.image.autocaption.exception.EmptyFileException;
import com.image.autocaption.exception.UnsupportedMediaTypeException;
import com.image.autocaption.service.AutoCaptionImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.image.autocaption.constant.ImageTypes.getSupportedContentTypes;

@RestController
@RequestMapping("/api/images")
@CrossOrigin
public class AutoCaptionImageResource {

    private final AutoCaptionImageService autoCaptionImageService;

    public AutoCaptionImageResource(AutoCaptionImageService autoCaptionImageService) {
        this.autoCaptionImageService = autoCaptionImageService;
    }

    @PostMapping(value = "/captions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> createCaptions(@RequestParam("image") MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new EmptyFileException("Uploaded file is empty");
        }

        String contentType = imageFile.getContentType();
        if (contentType.isEmpty() || !getSupportedContentTypes().containsKey(contentType)) {
            throw new UnsupportedMediaTypeException("Unsupported file type: " + contentType);
        }

        var captions = autoCaptionImageService.createCaptions(imageFile.getBytes(), imageFile);

        return ResponseEntity.ok(captions);
    }

}


