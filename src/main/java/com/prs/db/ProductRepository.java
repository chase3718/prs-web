package com.prs.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.prs.business.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	@Query("Select p from Product p where p.name like %:name%")
    Iterable<Product> findByNameContaining(@Param("name")String name);
	@Query("Select p from Product p where p.price <= :price")
	Iterable<Product> findByPriceLessThan(@Param("price")double price);
	@Query("Select p from Product p where p.price >= :price")
	Iterable<Product> findByPriceGreaterThan(@Param("price")double price);
}
