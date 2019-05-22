package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.PurchaseRequest;

public interface PurchaseRequestRepository extends CrudRepository<PurchaseRequest, Integer> {

}
