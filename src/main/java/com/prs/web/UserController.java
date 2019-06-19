package com.prs.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.prs.business.JsonResponse;
import com.prs.business.User;
import com.prs.db.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepository.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<User> p = userRepository.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(userRepository.findById(id));
			} else {
				jr = JsonResponse.getInstance("No user found for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody User user) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepository.save(user));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody User user) {
		JsonResponse jr = null;
		try {
			if (userRepository.existsById(user.getId())) {
				userRepository.delete(user);
				jr = JsonResponse.getInstance("User deleted");
			} else {
				jr = JsonResponse.getInstance("No User: " + user);
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
			if (userRepository.existsById(id)) {
				userRepository.deleteById(id);
				jr = JsonResponse.getInstance("User deleted");
			} else {
				jr = JsonResponse.getInstance("No User with id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody User user) {
		JsonResponse jr = null;
		try {
			if (userRepository.existsById(user.getId())) {
				jr = JsonResponse.getInstance(userRepository.save(user));
			} else {
				jr = JsonResponse.getInstance("No User exists with id: " + user.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PostMapping("/authenticate")
	public JsonResponse login(@RequestBody User user) {
		JsonResponse jr = null;
		String message = "";
		try {
			Optional<User> p = userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
			if (p.isPresent()) {
				jr = JsonResponse
						.getInstance(userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword()));
			} else {
				Optional<User> u = userRepository.findByUserName(user.getUserName());
				//Optional<User> pas = userRepository.findByPassword(user.getPassword());
				if (!u.isPresent()) {
					message += "No account exists with username " + user.getUserName();
				} else {
					message += "The password does not match the username " + user.getUserName();
				}
				jr = JsonResponse.getInstance(message);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
}