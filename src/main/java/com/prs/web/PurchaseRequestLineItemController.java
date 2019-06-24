package com.prs.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.prs.db.ProductRepository;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/purchase-request-line-items")
public class PurchaseRequestLineItemController {
	@Autowired
	private PurchaseRequestLineItemRepository prliRepo;
	@Autowired
	private PurchaseRequestRepository prRepo;
	@Autowired
	private ProductRepository productRepo;

	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prliRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequestLineItem> p = prliRepo.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(prliRepo.findById(id));
			} else {
				jr = JsonResponse.getInstance("No purchaseRequestLineItem found for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}
	
	@GetMapping("/from-purchase-request/{id}")
	public JsonResponse fromPurchaseRequest(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prliRepo.findByPurchaseRequestId(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		PurchaseRequest pr = prli.getPurchaseRequest();
		try {
			if (pr.getStatus().equals("New")) {
				
				jr = JsonResponse.getInstance(prliRepo.save(prli));
				updateTotal(prli);
			} else {
				jr = JsonResponse.getInstance("Purchase request status must be new");
			}

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody PurchaseRequestLineItem purchaseRequestLineItem) {
		JsonResponse jr = null;
		try {
			if (prliRepo.existsById(purchaseRequestLineItem.getId())) {
				prliRepo.delete(purchaseRequestLineItem);
				jr = JsonResponse.getInstance("PurchaseRequestLineItem deleted");
				updateTotal(purchaseRequestLineItem);
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequestLineItem: " + purchaseRequestLineItem);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@DeleteMapping("/{id}")
	public JsonResponse delete(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			if (prliRepo.existsById(id)) {
				jr = JsonResponse.getInstance(prliRepo.findById(id));
				prliRepo.findById(id).get().setQuantity(0);
				updateTotal(prliRepo.findById(id).get());
				prliRepo.deleteById(id);;
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequestLineItem by id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@Transactional
	@DeleteMapping("/delete-by-pr/{id}")
	public JsonResponse deleteByPr(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prliRepo.deleteByPurchaseRequestId(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody PurchaseRequestLineItem purchaseRequestLineItem) {
		JsonResponse jr = null;
		try {
			if (prliRepo.existsById(purchaseRequestLineItem.getId())) {
				prliRepo.save(purchaseRequestLineItem);
				updateTotal(purchaseRequestLineItem);
				jr = JsonResponse.getInstance(prliRepo.findById(purchaseRequestLineItem.getId()));
			} else {
				jr = JsonResponse
						.getInstance("No PurchaseRequestLineItem exists with id: " + purchaseRequestLineItem.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	private void updateTotal(PurchaseRequestLineItem prli) {
		PurchaseRequestLineItem full = prliRepo.findById(prli.getId()).get();
		PurchaseRequest pr = prRepo.findById(full.getPurchaseRequest().getId()).get();
		System.out.println(pr);
		BigDecimal total = new BigDecimal(0);
		try {
			Iterable<PurchaseRequestLineItem> lineItems = prliRepo.findAllByPurchaseRequestId(pr.getId());
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
		prRepo.save(pr);
	}
	
	
}