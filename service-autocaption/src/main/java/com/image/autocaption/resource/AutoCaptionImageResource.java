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

/**
 * The resource that exposes REST API for generating captions for input images.
 */
@RestController
@RequestMapping("/api/images")
public class AutoCaptionImageResource {

    private final AutoCaptionImageService autoCaptionImageService;

    public AutoCaptionImageResource(AutoCaptionImageService autoCaptionImageService) {
        this.autoCaptionImageService = autoCaptionImageService;
    }

    /**
     * The end point that generates a list of 3 captions for the input image. In case, an image is already available
     * in the in-memory cache, the pre-computed list of captions is result from the cache.
     *
     * @param imageFiles the image for which captions are to be generated
     * @return a response entity holding the list of captions generated and the HTTP status code
     * @throws IOException
     */
    @PostMapping(value = "/captions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> createCaptions(@RequestParam("image") MultipartFile[] imageFiles) throws IOException {
        if (imageFiles[0].isEmpty()) {
            throw new EmptyFileException("Uploaded file is empty");
        }

        String contentType = imageFiles[0].getContentType();
        if (contentType.isEmpty() || !getSupportedContentTypes().containsKey(contentType)) {
            throw new UnsupportedMediaTypeException("Unsupported file type: " + contentType);
        }

        var captions = autoCaptionImageService.createCaptions(imageFiles[0].getBytes(), imageFiles[0]);

        return ResponseEntity.ok(captions);
    }

}


