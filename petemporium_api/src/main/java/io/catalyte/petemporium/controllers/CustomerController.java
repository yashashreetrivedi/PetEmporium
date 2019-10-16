package io.catalyte.petemporium.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//Import statements acquire required classes
import io.catalyte.petemporium.domain.Customer;
import io.catalyte.petemporium.repositories.CustomerRepositorie;
import io.catalyte.petemporium.validation.ValidateControllers;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//Map all methods in this class to {root}/users
@RestController
@RequestMapping("/customers")
public class CustomerController {

	// Injects MongoDB collection on class initialization
	@Autowired
	CustomerRepositorie customerRepo;

	private Logger logger = LoggerFactory.getLogger(CustomerController.class);
	ValidateControllers validateControllers = new ValidateControllers();

	//Add new customer into customers collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation(" Create Customer in the system.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = Customer.class) })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		boolean isValid = validateCustomer(customer);
		if (isValid) {
			customerRepo.insert(customer);
			return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
		} else
			return new ResponseEntity<Customer>(HttpStatus.BAD_REQUEST);
	}
	
//Get all collection from customers collection 
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation("Get all customers in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Customer.class) })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getCustomer() {
		logger.debug(" The Current customers: " + customerRepo.findAll());
		List<Customer> customer = new ArrayList<Customer>();
		customer = customerRepo.findAll();
		logger.debug("Retrieved the current Customer");
		return new ResponseEntity<List<Customer>>(customer, HttpStatus.OK);

	}
	
//Get collection BY Id
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation("Get customer by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Customer.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomerById(@PathVariable String Id) {
		List<Customer> tmp = customerRepo.findAll();
		Iterator<Customer> it = tmp.iterator();
		while (it.hasNext()) {
			Customer customer = it.next();
			if (customer.get_id().equalsIgnoreCase(Id)) {
				return new ResponseEntity<>(customer, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	//Delete collection from the customers collection by ID
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation("Delete customer by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No Content", response = Customer.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
	public ResponseEntity deleteCustomerById(@PathVariable String Id) {
		List<Customer> tmp = customerRepo.findAll();
		Iterator<Customer> it = tmp.iterator();
		while (it.hasNext()) {
			Customer customer = it.next();
			if (customer.get_id().equalsIgnoreCase(Id)) {
				logger.info("Customer with ID of " + customer.get_id());
				customerRepo.deleteById(Id);
				return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
			}

		}
		return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{Id}", method = RequestMethod.PUT)
	public ResponseEntity<Customer> updateCustomer(@PathVariable String Id, @RequestBody Customer customer1) {

		List<Customer> customer = customerRepo.findAll();
		boolean isValid = validateCustomer(customer1);
		if (isValid) {
			Iterator<Customer> it = customer.iterator();
			while (it.hasNext()) {
				Customer c = it.next();
				if (c.get_id().equalsIgnoreCase(Id)) {
					if (validateCustomer(customer1)) {
						c.setFirstName(customer1.getFirstName());
						c.setLastName(customer1.getLastName());
						c.setPhoneNumber(customer1.getPhoneNumber());
				///	c.setAddress(customer1.getAddress().getCity());
						customerRepo.save(c);
						return new ResponseEntity<Customer>(customer1, HttpStatus.OK);
					}
				}
			}
		}
		return new ResponseEntity<Customer>(HttpStatus.BAD_REQUEST);
	}
//Get customer by lastname from the customer collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation("Get Customer by lastName by query in the system.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No Content", response = Customer.class) })
	@RequestMapping(value = "/name", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getEmployeeByLastName(@RequestParam("lastName") String lName) {
		logger.debug("Getting employees with last name = " + lName);
		List<Customer> tmp = new ArrayList<Customer>();
		tmp = customerRepo.findByLastName(lName);
		logger.debug("Retrieved the current inventory");
		return new ResponseEntity<List<Customer>>(tmp, HttpStatus.OK);
	}

	
	
	
	
	
	
	
	
	private boolean validateCustomer(Customer customer) {
		if (customer != null && validateControllers.validatePhoneNumber(customer.getPhoneNumber())
				&& validateControllers.validateEmail(customer.getEmail())
				&& validateControllers.validateZipCode(customer.getAddress().getZip())) {
			return true;
		} else {
			return false;
		}

	}

}
