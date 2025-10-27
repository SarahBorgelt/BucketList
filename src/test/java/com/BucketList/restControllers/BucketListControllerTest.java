package com.BucketList.restControllers;

import com.BucketList.entities.BucketItem;
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

@WebMvcTest(BucketListController.class)
public class BucketListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BucketListRepository repository;

    private BucketItem item1;
    private BucketItem item2;

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
        when(repository.findAll()).thenReturn(Arrays.asList(item1, item2));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Skydiving"))
                .andExpect(jsonPath("$[1].title").value("Visit Japan"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void createItem_ReturnsCreatedItem() throws Exception {
        when(repository.save(ArgumentMatchers.any(BucketItem.class))).thenReturn(item1);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Skydiving\", \"description\": \"Jump from a plane\", \"completed\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Skydiving"));

        verify(repository, times(1)).save(any(BucketItem.class));
    }

    @Test
    void updateItem_ValidId_UpdatesItem() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.of(item1));
        when(repository.save(any(BucketItem.class))).thenReturn(item1);

        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated\", \"description\": \"Updated desc\", \"completed\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(BucketItem.class));
    }

    @Test
    void updateItem_InvalidId_ReturnsError() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/items/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"No\", \"description\": \"No\", \"completed\": false}"))
                .andExpect(status().isNotFound());;

        verify(repository, times(1)).findById(999L);
    }

    @Test
    void deleteItem_RemovesFromRepository() throws Exception {
        doNothing().when(repository).deleteById(1L);

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1L);
    }

}

