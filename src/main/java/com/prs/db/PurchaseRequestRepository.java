package com.prs.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prs.business.PurchaseRequest;

public interface PurchaseRequestRepository extends CrudRepository<PurchaseRequest, Integer> {
	Iterable<PurchaseRequest> findByStatus(String status);
	@Query("Select pr from PurchaseRequest pr where id != :id and status = :status")
	Iterable<PurchaseRequest> findAllWithoutIdWithStatus(int id, String status);
}
