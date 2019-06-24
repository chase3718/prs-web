package com.prs.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prs.business.PurchaseRequest;
import com.prs.business.PurchaseRequestLineItem;

public interface PurchaseRequestLineItemRepository extends CrudRepository<PurchaseRequestLineItem, Integer> {
	Iterable<PurchaseRequestLineItem> findAllByPurchaseRequestId(int i);
	Iterable<PurchaseRequestLineItem> findByPurchaseRequestId(int id);
	Optional<PurchaseRequestLineItem> deleteByPurchaseRequestId(int id);
}
