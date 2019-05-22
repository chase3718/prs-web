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

import com.prs.business.User;
import com.prs.db.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserTests {
	@Autowired
	private UserRepository userRepo;
	
	private User u;
	@Test
	public void testUserGetAll() {
		Iterable<User> users = userRepo.findAll();
		assertNotNull(users);
	}

	@Before
	public void testUserAdd() {
		u = new User("username", "pwd", "fname", "lname", "phone", "email", true, true);
		assertNotNull(userRepo.save(u));
	}
	
	@Test
	public void testUserGet() {
		assertNotNull(userRepo.findById(u.getId()));
	}
	
	@Test
	public void testUserUpdate() {
		u.setUserName("UpdateUName");
		u.setFirstName("UpdateFName");
		assertNotNull(userRepo.save(u));
		assertEquals(u.getFirstName(), "UpdateFName");
	}
	
	@Test
	public void testUserLogin() {
		assertNotNull(userRepo.findByUserNameAndPassword(u.getUserName(), u.getPassword()));
	}
	
	@After
	public void testUserDelete() {
		userRepo.delete(u);
		assertFalse(userRepo.findById(u.getId()).isPresent());
	}
}
