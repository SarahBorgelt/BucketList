package com.BucketList.restControllers;

import com.BucketList.model.BucketItem;
import com.BucketList.repositories.BucketListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Tell Spring Boot to start a lightweight slice of the application focused on testing the web layer only
@WebMvcTest(BucketListController.class)
public class BucketListControllerTest {
    //@Autowired injects an instance automatically
    //MockMvc is a Spring test utility that lets you simulate HTTP requests to your controller without starting a server
    @Autowired
    private MockMvc mockMvc;

    //@MockBeans creates a mock instance of Spring bean and adds it to the application context
    @MockBean
    private BucketListRepository repository;

    private BucketItem item1;
    private BucketItem item2;

    //Set all of these factors before each test
    @BeforeEach
    void setUp() {
        item1 = new BucketItem();
        item1.setId(1L);
        item1.setTitle("Skydiving");
        item1.setDescription("Jump from a plane");
        item1.setCompleted(false);

        item2 = new BucketItem();
        item2.setId(2L);
        item2.setTitle("Visit Japan");
        item2.setDescription("Tokyo, Kyoto, Osaka");
        item2.setCompleted(true);
    }

    @Test
    void getAllItems_ReturnsList() throws Exception {
        //Mockito syntax that tells the mock repository (created with @MockBean) what to return when findAll() is called
        when(repository.findAll()).thenReturn(Arrays.asList(item1, item2));

        //Simulate an HTTP GET request to /api/items
        mockMvc.perform(get("/api/items"))

                //Is the HTTP response status code 200 OK?
                .andExpect(status().isOk())

                //Use JSONPath to verify the JSON response. $[0] is the first element of the returned array and .title is the title property.
                //.value checks that the value matches exactly.
                .andExpect(jsonPath("$[0].title").value("Skydiving"))
                .andExpect(jsonPath("$[1].title").value("Visit Japan"));

        //This ensures that the mocked repository method findAll() was called exactly once, which makes sure that the controller
        //called the repository as expected
        verify(repository, times(1)).findAll();
    }

    @Test
    void createItem_ReturnsCreatedItem() throws Exception {
        //This tells Mockito what to do when repository.save() is called.
        //Argument matchers.any(BucketItem.class) matches any BucketItem object.
        //The mock repository will return item1 whenever save is called, which allows me to
        //test the controller without touching a real database
        when(repository.save(ArgumentMatchers.any(BucketItem.class))).thenReturn(item1);

        //Simulates an HTTP POST request
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)

                        //Provides the JSON payload for the item being created to mimic what a client would send when adding a new bucket list item
                        .content("{\"title\": \"Skydiving\", \"description\": \"Jump from a plane\", \"completed\": false}"))

                //Is the HTTP response status code 201 Created?
                .andExpect(status().isCreated())

                //Check the returned JSON response. Does it match what we sent?
                .andExpect(jsonPath("$.title").value("Skydiving"));

        //This ensures that the mocked repository method findAll() was called exactly once, which makes sure that the controller
        //called the repository as expected
        verify(repository, times(1)).save(any(BucketItem.class));
    }

    @Test
    void updateItem_ValidId_UpdatesItem() throws Exception {
        //Returns item1 wrapped in Optional, simulating that the item exists in the database.
        //Optional is a container object that may or may not contain a non-null value
        when(repository.findById(1L)).thenReturn(Optional.of(item1));

        //Returns item1 whenever the controller tries to save an updated item
        when(repository.save(any(BucketItem.class))).thenReturn(item1);

        //mockMvc simulates a client sending an update request to /api/items/1
        mockMvc.perform(put("/api/items/1")
                        //Tells the server that the request body is JSON
                        .contentType(MediaType.APPLICATION_JSON)

                        //Provides the JSON payload
                        .content("{\"title\": \"Updated\", \"description\": \"Updated desc\", \"completed\": true}"))

                //Is the HTTP status 200 OK?
                .andExpect(status().isOk())

                //Does the returned JSON have the correct title?
                .andExpect(jsonPath("$.title").value("Updated"))

                //Is the completed field updated correctly?
                .andExpect(jsonPath("$.completed").value(true));

        //Was findById(1L) and save() called exactly once?
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(BucketItem.class));
    }

    @Test
    void updateItem_InvalidId_ReturnsError() throws Exception {
        //When someone requests id#999, pretend it doesn't exist. Optional.empty() simulates no value found.
        when(repository.findById(999L)).thenReturn(Optional.empty());

        //Simulate a client sending a PUT request to update object 999.
        mockMvc.perform(put("/api/items/999")
                        .contentType(MediaType.APPLICATION_JSON)

                        //This is the payload, which represents the new data that the client wants to update
                        .content("{\"title\": \"No\", \"description\": \"No\", \"completed\": false}"))

                //The controller should detect that the item does not exist
                .andExpect(status().isNotFound());;
        //Did the controller actually call findById(999L) exactly once?
        verify(repository, times(1)).findById(999L);
    }

    @Test
    void deleteItem_RemovesFromRepository() throws Exception {
        //Pretend that the item exists
        when(repository.existsById(1L)).thenReturn(true);

        //doNothing() tells that mock: "When deleteById(1L) is called, just pretend it succeeds and don't throw an exception."
        doNothing().when(repository).deleteById(1L);

        //Simulate a client sending a DELETE request to remove item with ID 1
        mockMvc.perform(delete("/api/items/1"))

                //Does the controller return HTTP 200 OK?
                .andExpect(status().isOk());

        //Did the controller call the method exactly once?
        verify(repository, times(1)).deleteById(1L);
    }

}

