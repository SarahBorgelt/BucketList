package com.BucketList.restControllers;

import com.BucketList.model.*;
import com.BucketList.repositories.BucketListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//Designate this as a request controller and request mapping to api/items pathway
//Use cross origin to allow the Spring Boot backend to accept requests from a different origin
// (domain, protocol, or port) than the backend itself.
@RestController
@RequestMapping("/api/items")
@CrossOrigin

public class BucketListController {

    //declare and initialize the repository object
    private final BucketListRepository repository;

    public BucketListController(BucketListRepository repository){
        this.repository = repository;
    }

    //Use @GetMapping to pull a list of all items using the findAll() method
    @GetMapping
    public List<BucketItem> getAll(){
        return repository.findAll();
    }

    // Handles HTTP POST requests to create a new bucket list item from the
    // request body and save it to the database.
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BucketItem create(@RequestBody BucketItem objectUpdate){
            return repository.save(objectUpdate);
    }


    //Use @PutMapping to update a bucket list item by id.
    @PutMapping("/{id}")
    public BucketItem update(@PathVariable Long id, @RequestBody BucketItem updated){
        //findById looks in the database for an item whose primary key matches the id passed in the URL
        //map only runs if an item with that id exists. If so, item represents the existing database records
        //and each section is updated accordingly.
        try{
            return repository.findById(id).map(item -> {
                item.setTitle(updated.getTitle());
                item.setDescription(updated.getDescription());
                item.setCompleted(updated.isCompleted());
                return repository.save(item);
                //Instruct map on what to do if the item cannot be found
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
            //Throw errors if the try is unable to be completed
        } catch (ResponseStatusException e){
            throw e;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating item", e);
        }
    }

    //Use @DeleteMapping to delete an item by id and save the changes to the database
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        try{
            if (!repository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
            }
            repository.deleteById(id);
        } catch (ResponseStatusException e){
            throw e;
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
