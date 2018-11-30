package com.teodora.ws.soap.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teodora.ws.soap.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
	
	
	

}
