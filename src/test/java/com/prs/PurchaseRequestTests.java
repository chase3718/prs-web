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

import com.prs.business.PurchaseRequest;
import com.prs.business.User;
import com.prs.db.PurchaseRequestRepository;
import com.prs.db.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PurchaseRequestTests {
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepo;
	@Autowired
	private UserRepository userRepo;
	
	private PurchaseRequest pr;
	private User u;
	@Test
	public void testPurchaseRequestGetAll() {
		Iterable<PurchaseRequest> purchaseRequests = purchaseRequestRepo.findAll();
		assertNotNull(purchaseRequests);
	}

	@Before
	public void testPurchaseRequestAdd() {
		u = new User("username", "pwd", "fname", "lname", "phone", "email", true, true);
		userRepo.save(u);
		pr = new PurchaseRequest(u, "TestDescription", "TestJustification", LocalDate.now(),
				"TestDeliveryMode", "TestStatus", 12.34, LocalDate.now(), null);
		assertNotNull(purchaseRequestRepo.save(pr));
	}
	
	@Test
	public void testPurchaseRequestGet() {
		assertNotNull(purchaseRequestRepo.findById(pr.getId()));
	}
	
	@Test
	public void testPurchaseRequestUpdate() {
		pr.setDescription("UpdatedDescription");
		pr.setJustification("UpdatedJustification");
		assertNotNull(purchaseRequestRepo.save(pr));
		assertEquals(pr.getDescription(), "UpdatedDescription");
	}
	
	@After
	public void testPurchaseRequestDelete() {
		purchaseRequestRepo.delete(pr);
		assertFalse(purchaseRequestRepo.findById(pr.getId()).isPresent());
	}
}
