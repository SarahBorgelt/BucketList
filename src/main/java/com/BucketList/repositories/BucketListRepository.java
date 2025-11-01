package com.BucketList.repositories;

import com.BucketList.model.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;

//By extending JpaRepository, Spring automatically provides standard CRUD operations without requiring SQL
//This can be injected at any time using @Autowired
public interface BucketListRepository extends JpaRepository<BucketItem, Long> {
}
