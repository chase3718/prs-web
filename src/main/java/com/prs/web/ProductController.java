package com.prs.web;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.prs.business.Vendor;
import com.prs.db.ProductRepository;
import com.prs.db.ProductTextFile;
import com.prs.storage.StorageService;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepository.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<Product> p = productRepository.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(productRepository.findById(id));
			} else {
				jr = JsonResponse.getInstance("No product found for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}
	
	@GetMapping("/from-vendor/{id}")
	public JsonResponse fromVendor(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepository.findByVendorId(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	private final Path rootLocation = Paths.get("upload-dir");
	
	@PostMapping("/from-file")
	public List<JsonResponse> addFromFile(@RequestBody Vendor vendor) {
		List<JsonResponse> jr = new ArrayList<JsonResponse>();
		ProductTextFile ptf = new ProductTextFile(rootLocation + "/products.csv");
		try {
			List<Product> products = ptf.getAll(vendor);
			for (Product p : products) {
				jr.add(JsonResponse.getInstance(productRepository.save(p)));
			}
		} catch (Exception e) {
			jr.add(JsonResponse.getInstance(e));
		}
		return jr;
	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody Product product) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepository.save(product));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody Product product) {
		JsonResponse jr = null;
		try {
			if (productRepository.existsById(product.getId())) {
				productRepository.delete(product);
				jr = JsonResponse.getInstance("Product deleted");
			} else {
				jr = JsonResponse.getInstance("No Product: " + product);
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
			if (productRepository.existsById(id)) {
				jr = JsonResponse.getInstance(productRepository.findById(id));
				productRepository.deleteById(id);
			} else {
				jr = JsonResponse.getInstance("No Product by id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody Product product) {
		JsonResponse jr = null;
		try {
			if (productRepository.existsById(product.getId())) {
				jr = JsonResponse.getInstance(productRepository.save(product));
			} else {
				jr = JsonResponse.getInstance("No Product exists with id: " + product.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/search")
	public JsonResponse searchProducts(@RequestParam String search) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepository.findByNameContaining(search));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/filtered-search")
	public JsonResponse fileredSearchProducts(@RequestParam String search, @RequestParam String priceFilter,
			@RequestParam double price, @RequestParam String vendor) {
		JsonResponse jr = null;
		Iterable<Product> products = productRepository.findByNameContaining(search);
		List<Product> filteredProducts = new ArrayList<>();
		try {
			if (!vendor.isEmpty() || vendor.equals("")) {
				for (Product p : products) {
					if (p.getVendor().getName().equalsIgnoreCase(vendor)) {
						if (priceFilter.equals(">")) {

							if (p.getPrice() >= price) {
								filteredProducts.add(p);
							}

						} else if (priceFilter.equals("<")) {

							if (p.getPrice() <= price) {
								filteredProducts.add(p);
							}

						} else {
							filteredProducts.add(p);
						}

					}
				}
			} else {
				for (Product p : products) {

					if (priceFilter.equals(">")) {

						if (p.getPrice() >= price) {
							filteredProducts.add(p);
						}

					} else if (priceFilter.equals("<")) {

						if (p.getPrice() <= price) {
							filteredProducts.add(p);
						}

					} else {
						filteredProducts.add(p);
					}

				}
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		if (filteredProducts.isEmpty()) {
			jr = JsonResponse.getInstance(products);
		} else {
			jr = JsonResponse.getInstance(filteredProducts);
		}
		return jr;
	}
}