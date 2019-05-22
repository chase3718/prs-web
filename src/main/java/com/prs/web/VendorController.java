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
import com.prs.business.Vendor;
import com.prs.db.VendorRepository;

@RestController
@RequestMapping("/vendors")
public class VendorController {
	@Autowired
	private VendorRepository vendorRepository;

	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(vendorRepository.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<Vendor> p = vendorRepository.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(vendorRepository.findById(id));
			} else {
				jr = JsonResponse.getInstance("No vendor found for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody Vendor vendor) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(vendorRepository.save(vendor));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody Vendor vendor) {
		JsonResponse jr = null;
		try {
			if (vendorRepository.existsById(vendor.getId())) {
				vendorRepository.delete(vendor);
				jr = JsonResponse.getInstance("Vendor deleted");
			} else {
				jr = JsonResponse.getInstance("No Vendor: " + vendor);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody Vendor vendor) {
		JsonResponse jr = null;
		try {
			if (vendorRepository.existsById(vendor.getId())) {
				jr = JsonResponse.getInstance(vendorRepository.save(vendor));
			} else {
				jr = JsonResponse.getInstance("No Vendor exists with id: " + vendor.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
}