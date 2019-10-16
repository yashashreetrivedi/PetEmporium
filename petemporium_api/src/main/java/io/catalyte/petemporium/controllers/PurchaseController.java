package io.catalyte.petemporium.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.catalyte.petemporium.domain.Customer;
import io.catalyte.petemporium.domain.Inventory;
import io.catalyte.petemporium.domain.Purchases;
import io.catalyte.petemporium.repositories.PurchaseRepositorie;
import io.catalyte.petemporium.validation.ValidateControllers;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
	// Injects MongoDB collection on class initialization
	@Autowired
	PurchaseRepositorie purchaseRepo;
	private Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	//call validation class 
	ValidateControllers validateControllers = new ValidateControllers();

	
	//Add a record in purchases collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation(" Create Purchases in the system.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = Purchases.class) })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Purchases> CreateInventory(@RequestBody Purchases purchases) {
//		boolean isValid = validatepurchaseCustomer(purchases.getCustomer());
//		double totalCalculatedTax= taxCalculation(purchases.getSubTotal(),purchases.getTaxPercentage());
//		double totalAmount= purchases.getSubTotal() + totalCalculatedTax;
		//purchases.setTotalPrice(totalAmount);
		//purchases.setTaxAmount(totalCalculatedTax);
		if (purchases != null ) {
			
			purchaseRepo.insert(purchases);
			return new ResponseEntity<Purchases>(purchases, HttpStatus.CREATED);
		}
		return new ResponseEntity<Purchases>(purchases, HttpStatus.BAD_REQUEST);

	}

	//Get all purchases record from the collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation(" Get all Purchases in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Purchases.class) })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Purchases>> getPurchases() {
		logger.debug("Purchases is: " + purchaseRepo.findAll());
		List<Purchases> purchases = new ArrayList<Purchases>();
		purchases = purchaseRepo.findAll();
		return new ResponseEntity<List<Purchases>>(purchases, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation(" Delete Purchase by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No Content", response = Purchases.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
	public ResponseEntity <Purchases>deletePutchasesById(@PathVariable String Id) {
		List<Purchases> tmp = purchaseRepo.findAll();
		Iterator<Purchases> it = tmp.iterator();
		while (it.hasNext()) {
			Purchases purchases = it.next();
			if (purchases.get_id().equalsIgnoreCase(Id)) {
				logger.info("Purchase with ID of " + purchases.get_id());
				purchaseRepo.deleteById(Id);
				return new ResponseEntity<Purchases>(HttpStatus.NO_CONTENT);
			}

		}
		return new ResponseEntity<Purchases>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation("Get Purchases by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Purchases.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Purchases> getPurchaseById(@PathVariable String Id) {
		List<Purchases> tmp = purchaseRepo.findAll();
		Iterator<Purchases> it = tmp.iterator();
		while (it.hasNext()) {
			Purchases purchase = it.next();
			if (purchase.get_id().equalsIgnoreCase(Id)) {
				return new ResponseEntity<Purchases>(purchase, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{Id}", method = RequestMethod.PUT)
	public ResponseEntity<Purchases> updateCustomer(@PathVariable String Id, @RequestBody Purchases purchase) {
		List<Purchases> pur = purchaseRepo.findAll();
		boolean isValid = validatepurchaseCustomer(purchase.getCustomer());
		if(isValid)
		{
		Iterator<Purchases> it = pur.iterator();
		while (it.hasNext()) {
			Purchases p = it.next();
			if (p.get_id().equalsIgnoreCase(Id)) {
				if (validatepurchaseCustomer(purchase.getCustomer())) {
					p.setCustomer(purchase.getCustomer());
					p.setDate(purchase.getDate());
					p.setItems(purchase.getItems());
					p.setTotalPrice(purchase.getTotalPrice());
					purchaseRepo.save(p);
					return new ResponseEntity<Purchases>(purchase, HttpStatus.OK);

				}
			}
		}
		}
		return new ResponseEntity<Purchases>(HttpStatus.BAD_REQUEST);
	}
	
	
	private boolean validatepurchaseCustomer(Customer customer) {
		if (customer != null && validateControllers.validatePhoneNumber(customer.getPhoneNumber())
				&& validateControllers.validateEmail(customer.getEmail())
				&& validateControllers.validateZipCode(customer.getAddress().getZip())) {
			return true;
		} else {
			return false;
		}
	}
	
	public Double taxCalculation( Double amount, Double taxPercent )
	{
		Double finalTax= amount * taxPercent/100;
		return finalTax;
	}

}