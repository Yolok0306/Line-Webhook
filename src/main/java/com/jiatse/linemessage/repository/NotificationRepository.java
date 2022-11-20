package com.jiatse.linemessage.repository;

import com.jiatse.linemessage.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query(value = "{ 'source.userId' : ?0 }")
    List<Notification> findBySourceUserId(final String userId);
}
