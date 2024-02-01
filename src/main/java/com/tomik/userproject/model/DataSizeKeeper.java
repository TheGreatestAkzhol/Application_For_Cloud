package com.tomik.userproject.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Document(collection = "dataSizeKeeper")
@Component
public class DataSizeKeeper {
    private Map<String, Integer> userMaxDataSizes = new HashMap<>();

    public int getMaxUserDataSize(String userID) {
        return userMaxDataSizes.getOrDefault(userID, Integer.MAX_VALUE);
    }

    public void setUserMaxDataSize(String userID, int maxUserDataSize) {
        userMaxDataSizes.put(userID, maxUserDataSize);
    }
}

