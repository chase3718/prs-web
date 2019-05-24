package com.prs.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.JsonResponse;
import com.prs.business.Product;
import com.prs.business.PurchaseRequest;
import com.prs.business.PurchaseRequestLineItem;
import com.prs.business.User;
import com.prs.db.ProductRepository;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;

@RestController
@RequestMapping("/purchaseRequests")
public class PurchaseRequestController {
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepository;
	@Autowired
	private PurchaseRequestLineItemRepository prliRepo;
	@Autowired
	private ProductRepository productRepo;

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

	@GetMapping("/list-review")
	public JsonResponse get(@RequestBody User user) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepository.findAllWithoutIdWithStatus(user.getId(), "Review"));
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

	@PostMapping("/submit-new")
	public JsonResponse submitNewPurchaseRequest(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			purchaseRequest.setStatus("New");
			updateTotal(purchaseRequest);
			jr = JsonResponse.getInstance(purchaseRequestRepository.save(purchaseRequest));

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/submit-review")
	public JsonResponse submitForReview(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			if (purchaseRequestRepository.existsById(purchaseRequest.getId())) {
				PurchaseRequest pr = purchaseRequestRepository.findById(purchaseRequest.getId()).get();
				updateTotal(pr);
				pr.setStatus("Review");
				if (pr.getTotal() <= 50) {
					pr.setStatus("Approved");
				}
				jr = JsonResponse.getInstance(purchaseRequestRepository.save(pr));
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequest exists with id: " + purchaseRequest.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/approve")
	public JsonResponse approve(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			if (purchaseRequestRepository.existsById(purchaseRequest.getId())) {
				PurchaseRequest pr = purchaseRequestRepository.findById(purchaseRequest.getId()).get();
				if (pr.getStatus().equals("Review")) {
					pr.setStatus("Aprroved");
					jr = JsonResponse.getInstance(purchaseRequestRepository.save(pr));
				} else {
					jr = JsonResponse.getInstance("Purchase Request must be in review");
				}
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequest exists with id: " + purchaseRequest.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/reject")
	public JsonResponse reject(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			if (purchaseRequest.getStatus().equals("Review")) {
				purchaseRequest.setStatus("Rejected");
				jr = JsonResponse.getInstance(purchaseRequestRepository.save(purchaseRequest));
			} else {
				jr = JsonResponse.getInstance("Purchase Request must be in review");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	@PutMapping("/reopen")
	public JsonResponse reopen(@RequestBody PurchaseRequest purchaseRequest) {
		JsonResponse jr = null;
		try {
			if (purchaseRequestRepository.existsById(purchaseRequest.getId())) {

				if (purchaseRequest.getStatus().equals("Rejected")) {
					purchaseRequest.setStatus("New");
					purchaseRequest.setReasonForRejection(null);
					jr = JsonResponse.getInstance(purchaseRequestRepository.save(purchaseRequest));
				} else {
					jr = JsonResponse.getInstance("Purchase Request must be rejected");
				}
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequest exists with id: " + purchaseRequest.getId());
			}
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

				updateTotal(purchaseRequest);
				jr = JsonResponse.getInstance(purchaseRequestRepository.save(purchaseRequest));
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequest exists with id: " + purchaseRequest.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	private void updateTotal(PurchaseRequest pr) {
		BigDecimal total = new BigDecimal(0);
		Iterable<PurchaseRequestLineItem> lineItems = prliRepo.findAllByPurchaseRequestId(pr.getId());
		try {
		for (PurchaseRequestLineItem li : lineItems) {
			Product p = productRepo.findById(li.getProduct().getId()).get();
			BigDecimal price = new BigDecimal(p.getPrice());
			BigDecimal quant = new BigDecimal(li.getQuantity());
			BigDecimal lineTotal = price.multiply(quant);
			total = total.add(lineTotal);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		total = total.setScale(2, RoundingMode.HALF_UP);
		pr.setTotal(total.doubleValue());
		purchaseRequestRepository.save(pr);
	}
}