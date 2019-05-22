package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.PurchaseRequestLineItem;

public interface PurchaseRequestLineItemRepository extends CrudRepository<PurchaseRequestLineItem, Integer> {

}
