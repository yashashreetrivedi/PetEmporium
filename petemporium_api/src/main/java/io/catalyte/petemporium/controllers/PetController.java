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
import io.catalyte.petemporium.domain.Pet;
import io.catalyte.petemporium.repositories.PetRepositorie;
import io.catalyte.petemporium.validation.ValidateControllers;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//Map all methods in this class to {root}/users
@RestController
@RequestMapping("/pets")
public class PetController {

	// Injects MongoDB collection on class initialization
	@Autowired
	PetRepositorie petRepo;
	private Logger logger = LoggerFactory.getLogger(PetController.class);
	// call validaeController class
	ValidateControllers validateControllers = new ValidateControllers();

	// Add record in pets collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(" Create pets in the system.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = Pet.class) })
	public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
		boolean isValid = validatedata(pet);
		if (isValid) {
			petRepo.insert(pet);
			return new ResponseEntity<Pet>(pet, HttpStatus.CREATED);
		}
		return new ResponseEntity<Pet>(pet, HttpStatus.BAD_REQUEST);
	}

	// Get all pets record from the pets collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation("Gets all pets in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Pet.class) })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Pet>> getPet() {
		logger.debug("pets: " + petRepo.findAll());
		List<Pet> pet = new ArrayList<Pet>();
		pet = petRepo.findAll();
		return new ResponseEntity<List<Pet>>(pet, HttpStatus.OK);
	}

	// Gets pet by Id from the collection
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@ApiOperation("Gets pet by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Pet.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Pet> getPetById(@PathVariable String Id) {
		List<Pet> tmp = petRepo.findAll();
		Iterator<Pet> it = tmp.iterator();
		while (it.hasNext()) {
			Pet pet = it.next();
			if (pet.get_id().equalsIgnoreCase(Id)) {
				return new ResponseEntity<Pet>(pet, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
	}

	// Delete one pet record fron collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation("Delete pet by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Not Content", response = Pet.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
	public ResponseEntity<Pet> deletePetById(@PathVariable String Id) {
		List<Pet> tmp = petRepo.findAll();
		Iterator<Pet> it = tmp.iterator();
		while (it.hasNext()) {
			Pet pet = it.next();
			if (pet.get_id().equalsIgnoreCase(Id)) {
				logger.info("Pet with ID of " + pet.get_id());
				petRepo.deleteById(Id);
				return new ResponseEntity<Pet>(HttpStatus.NO_CONTENT);
			}

		}
		return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
	}

	// update a record fromthe pet collection by ID
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(" update pet by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Pet.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.PUT)
	public ResponseEntity<Pet> updatePetById(@Valid @PathVariable String Id, @Valid @RequestBody Pet pet) {
		if (pet.toString().contains("=,")) {
			return new ResponseEntity<Pet>(HttpStatus.BAD_REQUEST);

		}
		for (Pet p : petRepo.findAll()) {
			if (p.get_id().equalsIgnoreCase(Id)) {
				petRepo.save(pet);
				return new ResponseEntity<Pet>(pet, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
	}

	// Get pets by name
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation("Get pets by name by query in the system.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No Content", response = Customer.class) })
	@RequestMapping(value = "/name", method = RequestMethod.GET)
	public ResponseEntity<List<Pet>> getpetByName(@RequestParam("name") String name) {
		logger.debug("Getting pets with name = " + name);
		List<Pet> tmp = new ArrayList<Pet>();
		tmp = petRepo.findByName(name);
		return new ResponseEntity<List<Pet>>(tmp, HttpStatus.OK);
	}

	// validation for pet fields
	public boolean validatedata(Pet pet) {
		if (pet != null && validateControllers.validatePet(pet.get_id(), pet.getName(), pet.getSex(), pet.getColor(),
				pet.getAge())) {
			return true;
		}
		return false;
	}
}
