package com.tomik.userproject.service;

import com.tomik.userproject.model.DataSizeKeeper;
import com.tomik.userproject.model.User;
import com.tomik.userproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ServiceTestImpl {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private DataSizeKeeper dataSizeKeeperMock;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserDataShouldSaveDataSuccessfully() {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";
        String data = "Tamura";

        when(dataSizeKeeperMock.getMaxUserDataSize(userID)).thenReturn(1000);
        when(userRepositoryMock.save(any(User.class))).thenReturn(new User(userID, recordID, data));

        ResponseEntity<String> response = userService.saveUserData(userID, recordID, data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data saved successfully", response.getBody());

        verify(userRepositoryMock, times(1)).save(any(User.class));
    }

    @Test
    void saveUserDataShouldHandleDataSizeExceedsLimit() {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";
        String data = "Tamura";

        when(dataSizeKeeperMock.getMaxUserDataSize(userID)).thenReturn(5); // Set a small size limit

        ResponseEntity<String> response = userService.saveUserData(userID, recordID, data);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Data size exceeds the limit", response.getBody());

        verify(userRepositoryMock, never()).save(any(User.class));
    }


    @Test
    void getUserShouldReturnUserIfExists() {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";
        User mockUser = new User(userID, recordID, "Some data");

        when(userRepositoryMock.findByUserIDAndRecordID(userID, recordID)).thenReturn(mockUser);

        User result = userService.getUser(userID, recordID);

        assertEquals(mockUser, result);
    }

    @Test
    void getUserShouldReturnNullIfUserDoesNotExist() {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";

        when(userRepositoryMock.findByUserIDAndRecordID(userID, recordID)).thenReturn(null);

        User result = userService.getUser(userID, recordID);

        assertNull(result);
    }

}
