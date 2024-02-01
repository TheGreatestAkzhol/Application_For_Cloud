package com.tomik.userproject.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Size(max = 256)
    private String userID;
    @Size(max = 256)
    private String recordID;
    private String data;
    @LastModifiedDate
    private Date lastModifiedDate;
    @CreatedDate
    private Date createdDate;

    public User() {
    }

    public User(String userID, String recordID, String data) {
        this.userID = userID;
        this.recordID = recordID;
        this.data = data;
    }

}
