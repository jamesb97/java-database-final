package com.project.code.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.code.Model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByid(Long id);

    @Query("SELECT p FROM Store p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    List<Store> findBySubName(String pname);

}

// package com.project.code.Repo;

// public interface StoreRepository {
// 1. Add the repository interface:
// - Extend JpaRepository<Store, Long> to inherit basic CRUD functionality.
// - This allows the repository to perform operations like save, delete, update,
// and find without having to implement these methods manually.

// Example: public interface StoreRepository extends JpaRepository<Store, Long>
// {}

// 2. Add custom query methods:
// - **findById**:
// - This method will retrieve a store by its ID.
// - Return type: Store
// - Parameter: Long id

// Example: public Store findById(Long id);

// - **findBySubName**:
// - This method will retrieve stores whose name contains a given substring.
// - Return type: List<Store>
// - Parameter: String pname
// - Use @Query annotation to write a custom query.

// }
