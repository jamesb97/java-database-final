package com.project.code.Repo;

import com.project.code.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem o WHERE o.product.id = :productId")
    void deleteByProductId(Long productId);

}

// package com.project.code.Repo;

// public interface OrderItemRepository {
// 1. Add the repository interface:
// - Extend JpaRepository<OrderItem, Long> to inherit basic CRUD functionality.
// - This allows the repository to perform operations like save, delete, update,
// and find without having to implement these methods manually.

// Example: public interface OrderItemRepository extends
// JpaRepository<OrderItem, Long> {}

// 2. Since no custom methods are required for this repository, the default CRUD
// operations (save, delete, update, findById, etc.) are available out of the
// box.

// }
