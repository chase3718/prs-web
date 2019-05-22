package com.prs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prs.business.Vendor;
import com.prs.db.VendorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class VendorTests {
	@Autowired
	private VendorRepository vendorRepo;
	
	private Vendor u;
	@Test
	public void testVendorGetAll() {
		Iterable<Vendor> vendors = vendorRepo.findAll();
		assertNotNull(vendors);
	}

	@Before
	public void testVendorAdd() {
		u = new Vendor("testcode", "tester", "123 test st", "testville", "ts", "testz", "xxx-xxx-xxxx",
				"test@test.com", true);
		assertNotNull(vendorRepo.save(u));
	}
	
	@Test
	public void testVendorGet() {
		assertNotNull(vendorRepo.findById(u.getId()));
	}
	
	@Test
	public void testVendorUpdate() {
		u.setName("UpdateName");
		u.setCode("UpdateFName");
		assertNotNull(vendorRepo.save(u));
		assertEquals(u.getName(), "UpdateName");
	}
	
	@After
	public void testVendorDelete() {
		vendorRepo.delete(u);
		assertFalse(vendorRepo.findById(u.getId()).isPresent());
	}
}
