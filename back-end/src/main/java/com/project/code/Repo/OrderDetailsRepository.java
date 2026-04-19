package com.project.code.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.code.Model.OrderDetails;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
}

// 1. Add the repository interface:
//    - Extend JpaRepository<OrderDetails, Long> to inherit basic CRUD functionality.
//    - This allows the repository to perform operations like save, delete, update, and find without having to implement these methods manually.
//
// 2. Since no custom methods are required for this repository, the default CRUD operations (save, delete, update, findById, etc.) are available out of the box.
