package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.JsonResponse;
import com.prs.business.PurchaseRequestLineItem;
import com.prs.db.PurchaseRequestLineItemRepository;

@RestController
@RequestMapping("/purchaseRequestLineItems")
public class PurchaseRequestLineItemController {
	@Autowired
	private PurchaseRequestLineItemRepository purchaseRequestLineItemRepository;

	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepository.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequestLineItem> p = purchaseRequestLineItemRepository.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(purchaseRequestLineItemRepository.findById(id));
			} else {
				jr = JsonResponse.getInstance("No purchaseRequestLineItem found for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody PurchaseRequestLineItem purchaseRequestLineItem) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepository.save(purchaseRequestLineItem));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody PurchaseRequestLineItem purchaseRequestLineItem) {
		JsonResponse jr = null;
		try {
			if (purchaseRequestLineItemRepository.existsById(purchaseRequestLineItem.getId())) {
				purchaseRequestLineItemRepository.delete(purchaseRequestLineItem);
				jr = JsonResponse.getInstance("PurchaseRequestLineItem deleted");
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequestLineItem: " + purchaseRequestLineItem);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody PurchaseRequestLineItem purchaseRequestLineItem) {
		JsonResponse jr = null;
		try {
			if (purchaseRequestLineItemRepository.existsById(purchaseRequestLineItem.getId())) {
				jr = JsonResponse.getInstance(purchaseRequestLineItemRepository.save(purchaseRequestLineItem));
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequestLineItem exists with id: " + purchaseRequestLineItem.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
}