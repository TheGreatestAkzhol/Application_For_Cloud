package com.tomik.userproject.controller;

import com.tomik.userproject.model.User;
import com.tomik.userproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class Controller {

    private UserService userService;

    @PostMapping("/set/{userID}/{recordID}")
    public ResponseEntity<String> setData(
            @PathVariable String userID,
            @PathVariable String recordID,
            @RequestBody String requestData) {
        try {
            return userService.saveUserData(userID, recordID, requestData);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving data: " + e.getMessage());
        }
    }
    @PostMapping("/setLimit/{userID}")
    public ResponseEntity<String> setUserDataLimit(
            @PathVariable String userID,
            @RequestParam int maxUserDataSize) {
        try {
            userService.setUserMaxDataSize(userID, maxUserDataSize);
            return ResponseEntity.ok("User data size limit set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error setting user data size limit: " + e.getMessage());
        }
    }
    @GetMapping("/get/{userID}/{recordID}")
    public ResponseEntity<String> getData(
            @PathVariable String userID,
            @PathVariable String recordID) {

        try {
            User user = userService.getUser(userID,recordID);
            if(user != null){
                return ResponseEntity.ok(user.getData());
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Required data not found!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving data: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{userID}")
    public ResponseEntity<String> deleteLastRecord(
            @PathVariable String userID
    ){
        try{
            userService.removeOldestRecord(userID);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving data: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Last record successfully deleted");
    }
}

