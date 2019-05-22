package com.prs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prs.business.Product;
import com.prs.business.Vendor;
import com.prs.db.ProductRepository;
import com.prs.db.ProductTextFile;
import com.prs.db.VendorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductTests {
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private VendorRepository vendorRepo;
	private Vendor v;
	private Product p;

	@Test
	public void testProductGetAll() {
		Iterable<Product> products = productRepo.findAll();
		assertNotNull(products);
	}

	@Before
	public void testProductAdd() {
		v = new Vendor("testcode", "tester", "123 test st", "testville", "ts", "testz", "xxx-xxx-xxxx", "test@test.com",
				true);
		vendorRepo.save(v);
		p = new Product(v, "TestPNum", "Tester", 12.34, "TestUnit", "TestPhotoPath");
		assertNotNull(productRepo.save(p));
	}

	@Test
	public void testProductFromFile() {
		ProductTextFile ptf = new ProductTextFile("productstest.csv");
		List<Product> products = ptf.getAll(v);
		for (Product p : products) {
			assertNotNull(productRepo.save(p));
		}
		
	}

	@Test
	public void testProductGet() {
		assertNotNull(productRepo.findById(p.getId()));
	}

	@Test
	public void testProductUpdate() {
		p.setName("UpdateName");
		p.setPartNumber("UpdateNum");
		assertNotNull(productRepo.save(p));
		assertEquals(p.getName(), "UpdateName");
	}

	@After
	public void testProductDelete() {
		productRepo.delete(p);
		assertFalse(productRepo.findById(p.getId()).isPresent());
	}
}
