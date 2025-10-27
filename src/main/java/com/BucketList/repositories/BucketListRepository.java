package com.BucketList.repositories;

import com.BucketList.entities.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketListRepository extends JpaRepository<BucketItem, Long> {
}
