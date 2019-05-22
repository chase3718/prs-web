package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Vendor;

public interface VendorRepository extends CrudRepository<Vendor, Integer> {
	
}
