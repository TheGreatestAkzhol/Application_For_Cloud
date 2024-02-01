package com.tomik.userproject.controller;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tomik.userproject.model.User;
import com.tomik.userproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTestImpl {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userServiceMock;
    @BeforeEach
    public void setUp() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }
    @Test
    void setDataMethodShouldSuccessfullySaveData() throws Exception {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";
        String requestData = "Tamura";

        when(userServiceMock.saveUserData(any(), any(), any())).thenReturn(ResponseEntity.ok("Data saved successfully"));

        mockMvc.perform(post("/api/set/{userID}/{recordID}", userID, recordID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData))
                .andExpect(status().isOk())
                .andExpect(content().string("Data saved successfully"));

        verify(userServiceMock, times(1)).saveUserData(eq(userID), eq(recordID), eq(requestData));
    }

    @Test
    void setDataMethodShouldHandleError() throws Exception {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";
        String requestData = "Tamura";

        when(userServiceMock.saveUserData(any(), any(), any())).thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(post("/api/set/{userID}/{recordID}", userID, recordID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error saving data: Internal Server Error"));
    }
    @Test
    void setUserDataLimitMethodShouldSetLimitSuccessfully() throws Exception {
        String userID = "pabloEscobar";
        int maxUserDataSize = 1000;

        mockMvc.perform(post("/api/setLimit/{userID}", userID)
                        .param("maxUserDataSize", String.valueOf(maxUserDataSize)))
                .andExpect(status().isOk())
                .andExpect(content().string("User data size limit set successfully"));

        verify(userServiceMock, times(1)).setUserMaxDataSize(eq(userID), eq(maxUserDataSize));
    }

    @Test
    void setUserDataLimitMethodShouldHandleError() throws Exception {
        String userID = "pabloEscobar";
        int maxUserDataSize = 1000;

        doThrow(new RuntimeException("Internal Server Error"))
                .when(userServiceMock).setUserMaxDataSize(any(), anyInt());

        mockMvc.perform(post("/api/setLimit/{userID}", userID)
                        .param("maxUserDataSize", String.valueOf(maxUserDataSize)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error setting user data size limit: Internal Server Error"));
    }

    @Test
    void getDataMethodShouldReturnDataSuccessfully() throws Exception {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";
        String responseData = "Some data";

        when(userServiceMock.getUser(any(), any())).thenReturn(new User(userID,recordID,responseData));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get/{userID}/{recordID}", userID, recordID))
                .andExpect(status().isOk())
                .andExpect(content().string(responseData));
    }

    @Test
    void getDataMethodShouldHandleNotFound() throws Exception {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";

        when(userServiceMock.getUser(any(), any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get/{userID}/{recordID}", userID, recordID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Required data not found!"));
    }

    @Test
    void getDataMethodShouldHandleError() throws Exception {
        String userID = "pabloEscobar";
        String recordID = "JordanBelford";

        when(userServiceMock.getUser(any(), any())).thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get/{userID}/{recordID}", userID, recordID))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error retrieving data: Internal Server Error"));
    }

    @Test
    void deleteLastRecordMethodShouldDeleteSuccessfully() throws Exception {
        String userID = "pabloEscobar";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/{userID}", userID))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Last record successfully deleted"));

        verify(userServiceMock, times(1)).removeOldestRecord(eq(userID));
    }

    @Test
    void deleteLastRecordMethodShouldHandleError() throws Exception {
        String userID = "pabloEscobar";

        doThrow(new RuntimeException("Internal Server Error"))
                .when(userServiceMock).removeOldestRecord(any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/{userID}", userID))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error retrieving data: Internal Server Error"));
    }

}
