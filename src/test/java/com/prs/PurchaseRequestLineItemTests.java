package com.prs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prs.business.Product;
import com.prs.business.PurchaseRequest;
import com.prs.business.PurchaseRequestLineItem;
import com.prs.business.User;
import com.prs.business.Vendor;
import com.prs.db.ProductRepository;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;
import com.prs.db.UserRepository;
import com.prs.db.VendorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PurchaseRequestLineItemTests {
	@Autowired
	private PurchaseRequestLineItemRepository purchaseRequestLineItemRepo;
	@Autowired
	private PurchaseRequestRepository prRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private VendorRepository vendorRepo;
	@Autowired
	private UserRepository userRepo;
	
	private PurchaseRequestLineItem prli;
	private PurchaseRequest pr;
	private Product p;
	private Vendor v;
	private User u;
	@Test
	public void testPurchaseRequestLineItemGetAll() {
		Iterable<PurchaseRequestLineItem> purchaseRequestLineItems = purchaseRequestLineItemRepo.findAll();
		assertNotNull(purchaseRequestLineItems);
	}

	@Before
	public void testPurchaseRequestLineItemAdd() {
		u = new User("username", "pwd", "fname", "lname", "phone", "email", true, true);
		userRepo.save(u);
		v = new Vendor("testcode", "tester", "123 test st", "testville", "ts", "testz", "xxx-xxx-xxxx",
				"test@test.com", true);
		vendorRepo.save(v);
		p = new Product(v, "TestPNum", "Tester", 12.34, "TestUnit", "TestPhotoPath");
		productRepo.save(p);
		pr = new PurchaseRequest(u, "TestDescription", "TestJustification", LocalDate.now(),
				"TestDeliveryMode", "TestStatus", 12.34, LocalDate.now(), null);
		prRepo.save(pr);
		prli = new PurchaseRequestLineItem(pr, p, 10);
		assertNotNull(purchaseRequestLineItemRepo.save(prli));
	}
	
	@Test
	public void testPurchaseRequestLineItemGet() {
		assertNotNull(purchaseRequestLineItemRepo.findById(prli.getId()));
	}
	
	@Test
	public void testPurchaseRequestLineItemUpdate() {
		prli.setQuantity(5);
		assertNotNull(purchaseRequestLineItemRepo.save(prli));
		assertEquals(prli.getQuantity(), 5);
	}
	
	@After
	public void testPurchaseRequestLineItemDelete() {
		purchaseRequestLineItemRepo.delete(prli);
		assertFalse(purchaseRequestLineItemRepo.findById(prli.getId()).isPresent());
	}
}
