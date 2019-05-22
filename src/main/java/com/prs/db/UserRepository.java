package com.prs.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByUserNameAndPassword(String userName, String password);
}
