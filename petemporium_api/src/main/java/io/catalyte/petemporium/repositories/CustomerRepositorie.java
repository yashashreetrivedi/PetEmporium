package io.catalyte.petemporium.repositories;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.catalyte.petemporium.domain.Customer;
public interface CustomerRepositorie extends  MongoRepository<Customer, String>  {
	//public Customer findByFirstName(String firstName);
	public List<Customer> findByLastName(String lastName);
	
}
