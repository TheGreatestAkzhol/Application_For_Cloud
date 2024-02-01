package com.tomik.userproject.repository;

import com.tomik.userproject.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserIDAndRecordID(String userID, String recordID);
    User findByUserID(String userId);
    List<User> findByUserIDOrderByLastModifiedDateAsc(String userId);
}
