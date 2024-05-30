package com.example.shopbe.repositories;

import com.example.shopbe.models.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    // check product exist or not
    boolean existsByName(String name);

    // paging
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p from Product p WHERE " +
    "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
    "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> searchProducts(@Param("keyword") String keyword, @Param("categoryId") Long categoryId, Pageable pageable);
}
