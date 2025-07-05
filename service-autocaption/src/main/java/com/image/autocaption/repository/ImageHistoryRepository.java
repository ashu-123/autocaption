package com.image.autocaption.repository;

import com.image.autocaption.model.entity.ImageHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageHistoryRepository extends MongoRepository<ImageHistory, ObjectId> {

    List<ImageHistory> findByUserId(String userId);
}
