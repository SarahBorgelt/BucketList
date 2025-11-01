package com.BucketList.entities;

import com.BucketList.model.BucketItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BucketItemTest {

    private BucketItem item;

    @BeforeEach
    void setUp() {
        item = new BucketItem();
    }

    @Test
    void testSetAndGetId() {
        item.setId(1L);
        assertEquals(1L, item.getId(), "ID should be set and retrieved correctly");
    }

    @Test
    void testSetAndGetTitle() {
        String title = "Visit Japan";
        item.setTitle(title);
        assertEquals(title, item.getTitle(), "Title should be set and retrieved correctly");
    }

    @Test
    void testSetAndGetDescription() {
        String description = "See cherry blossoms in Kyoto";
        item.setDescription(description);
        assertEquals(description, item.getDescription(), "Description should be set and retrieved correctly");
    }

    @Test
    void testSetAndGetCompleted() {
        item.setCompleted(true);
        assertTrue(item.isCompleted(), "Completed flag should be true when set to true");

        item.setCompleted(false);
        assertFalse(item.isCompleted(), "Completed flag should be false when set to false");
    }

    @Test
    void testDefaultValues() {
        assertNull(item.getId(), "Default ID should be null before being set");
        assertNull(item.getTitle(), "Default title should be null before being set");
        assertNull(item.getDescription(), "Default description should be null before being set");
        assertFalse(item.isCompleted(), "Default completed should be false");
    }

    @Test
    void testEqualityById() {
        BucketItem item1 = new BucketItem();
        BucketItem item2 = new BucketItem();

        item1.setId(1L);
        item2.setId(1L);

        // Entities are often compared by ID in persistence contexts
        assertEquals(item1.getId(), item2.getId(), "Items with same ID should be considered equal by ID value");
    }
}
