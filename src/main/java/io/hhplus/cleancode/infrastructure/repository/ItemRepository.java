package io.hhplus.cleancode.infrastructure.repository;

import io.hhplus.cleancode.infrastructure.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, String> {
}
