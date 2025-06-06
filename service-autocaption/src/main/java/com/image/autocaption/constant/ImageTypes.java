package com.image.autocaption.constant;

import org.springframework.util.MimeType;

import java.util.Map;

import static org.springframework.http.MediaType.*;

public class ImageTypes {

    public static Map<String, MimeType> getSupportedContentTypes() {
        return Map.of("image/png", IMAGE_PNG,
                "image/jpeg", IMAGE_JPEG,
                "image/jpg", IMAGE_JPEG,
                "image/gif", IMAGE_GIF);
    }
}
