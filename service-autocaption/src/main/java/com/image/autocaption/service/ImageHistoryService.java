package com.image.autocaption.service;

import com.image.autocaption.model.entity.ImageHistory;
import com.image.autocaption.repository.ImageHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ImageHistoryService {

    private final ImageHistoryRepository imageHistoryRepository;

    public ImageHistoryService(ImageHistoryRepository imageHistoryRepository) {
        this.imageHistoryRepository = imageHistoryRepository;
    }

    public ImageHistory createImageHistory(String imageUrl, List<String> captions) {
        ImageHistory imageHistory = new ImageHistory();
        imageHistory.setImageId("IMG_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "_"
                + UUID.randomUUID().toString().substring(0, 8));
        imageHistory.setImageUrl(imageUrl);
        imageHistory.setCaptions(captions);
        imageHistory.setUploadedAt(LocalDateTime.now());
        imageHistory.setModelUsed("gemma3:4b");
        imageHistory.setUserId("ashu123");
        return imageHistoryRepository.save(imageHistory);
    }

    public List<ImageHistory> findByUserId(String userId) {
        return imageHistoryRepository.findByUserId(userId);
    }
}
