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
import com.prs.business.PurchaseRequest;
import com.prs.db.PurchaseRequestRepository;

@RestController
@RequestMapping("/purchaseRequests")
public class PurchaseRequestController {
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepository;

	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepository.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequest> p = purchaseRequestRepository.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(purchaseRequestRepository.findById(id));
			} else {
				jr = JsonResponse.getInstance("No purchaseRequest found for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepository.save(purchaseRequest));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			if (purchaseRequestRepository.existsById(purchaseRequest.getId())) {
				purchaseRequestRepository.delete(purchaseRequest);
				jr = JsonResponse.getInstance("PurchaseRequest deleted");
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequest: " + purchaseRequest);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			if (purchaseRequestRepository.existsById(purchaseRequest.getId())) {
				jr = JsonResponse.getInstance(purchaseRequestRepository.save(purchaseRequest));
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequest exists with id: " + purchaseRequest.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
}