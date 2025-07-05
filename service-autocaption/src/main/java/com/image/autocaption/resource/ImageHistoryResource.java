package com.image.autocaption.resource;

import com.image.autocaption.model.entity.ImageHistory;
import com.image.autocaption.service.ImageHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ImageHistoryResource {

    private final ImageHistoryService imageHistoryService;

    public ImageHistoryResource(ImageHistoryService imageHistoryService) {
        this.imageHistoryService = imageHistoryService;
    }

    @GetMapping("/{userId}/images")
    public ResponseEntity<List<ImageHistory>> getImageHistory(@PathVariable String userId) {
        return ResponseEntity.ok(imageHistoryService.findByUserId(userId));
    }
}
