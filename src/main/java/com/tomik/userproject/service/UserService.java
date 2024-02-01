package com.tomik.userproject.service;

import com.tomik.userproject.model.DataSizeKeeper;
import com.tomik.userproject.model.User;
import com.tomik.userproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private DataSizeKeeper dataSizeKeeper;

    public ResponseEntity<String> saveUserData(String userID, String recordID, String data) {
        try {

            if (isUserDataSizeWithinLimit(userID, data)) {
                User user = new User(userID, recordID, data);
                userRepository.save(user);
                return ResponseEntity.ok("Data saved successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Data size exceeds the limit");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving data: " + e.getMessage());
        }
    }

    public User getUser(String userID, String recordID) {
        return userRepository.findByUserIDAndRecordID(userID, recordID);
    }

    public int getUserDataSize(String userID) {
        User user = userRepository.findByUserID(userID);
        return (user != null && user.getData() != null) ? user.getData().length() : 0;
    }
    public Date getLastModifiedDate(String userID, String recordID) {
        User user = userRepository.findByUserIDAndRecordID(userID, recordID);
        return (user != null) ? user.getLastModifiedDate() : null;
    }

    public Date getCreatedDate(String userID, String recordID) {
        User user = userRepository.findByUserIDAndRecordID(userID, recordID);
        return (user != null) ? user.getCreatedDate() : null;
    }
    public void setUserMaxDataSize(String userID, int maxUserDataSize) {
        dataSizeKeeper.setUserMaxDataSize(userID, maxUserDataSize);
    }
    public boolean isUserDataSizeWithinLimit(String userID, String data) {
        int currentDataSize = getUserDataSize(userID);
        int maxDataSize = dataSizeKeeper.getMaxUserDataSize(userID);
        return currentDataSize + data.length() <= maxDataSize;
    }
    public void removeOldestRecord(String userID) {
        List<User> userRecords = userRepository.findByUserIDOrderByLastModifiedDateAsc(userID);
        if (!userRecords.isEmpty()) {
            User oldestRecord = userRecords.get(0);
            userRepository.delete(oldestRecord);
        }
    }
}

