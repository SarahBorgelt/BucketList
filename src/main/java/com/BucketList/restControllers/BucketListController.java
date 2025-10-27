package com.BucketList.restControllers;

import com.BucketList.entities.*;
import com.BucketList.repositories.BucketListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin
public class BucketListController {

    private final BucketListRepository repository;

    public BucketListController(BucketListRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<BucketItem> getAll(){
        return repository.findAll();
    }

    @PostMapping
    public BucketItem create(@RequestBody BucketItem updated){
        return repository.save(updated);
    }

    @PutMapping("/{id}")
    public BucketItem update(@PathVariable Long id, @RequestBody BucketItem updated){
        return repository.findById(id).map(item -> {
            item.setTitle(updated.getTitle());
            item.setDescription(updated.getDescription());
            item.setCompleted(updated.isCompleted());
            return repository.save(item);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        repository.deleteById(id);
    }
}
