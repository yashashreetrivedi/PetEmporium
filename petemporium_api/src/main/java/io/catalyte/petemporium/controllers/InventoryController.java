package io.catalyte.petemporium.controllers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import io.catalyte.petemporium.domain.Inventory;
import io.catalyte.petemporium.repositories.InventoryRepositorie;
import io.catalyte.petemporium.validation.ValidateControllers;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//Map all methods in this class to {root}/users
@RestController
@RequestMapping("/inventory")
public class InventoryController {

	// Injects MongoDB collection on class initialization
	@Autowired
	InventoryRepositorie inventoryRepo;

	private Logger logger = LoggerFactory.getLogger(InventoryController.class);
	ValidateControllers validateControllers = new ValidateControllers();

	// Add a inventory record in inventory collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation(" Create Inventory in the system.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = Inventory.class) })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Inventory> CreateInventory(@RequestBody Inventory inventory) {
		boolean isValid = validateInventory(inventory);
		if (isValid) {
			inventoryRepo.insert(inventory);
			return new ResponseEntity<Inventory>(inventory, HttpStatus.CREATED);
		}
		return new ResponseEntity<Inventory>(inventory, HttpStatus.BAD_REQUEST);

	}

	// Get all Inventroy from the inventory record
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation(" Get all Inventory in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Inventory.class) })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Inventory>> getInventory() {
		List<Inventory> inventory = new ArrayList<Inventory>();
		inventory = inventoryRepo.findAll();
		return new ResponseEntity<List<Inventory>>(inventory, HttpStatus.OK);
	}

	// Delete inventory by Id from the system
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ApiOperation(" Delete Inventory by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No Content", response = Inventory.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
	public ResponseEntity<Inventory> deleteInventoryById(@PathVariable String Id) {
		List<Inventory> tmp = inventoryRepo.findAll();
		Iterator<Inventory> it = tmp.iterator();
		while (it.hasNext()) {
			Inventory inventory = it.next();
			if (inventory.get_id().equalsIgnoreCase(Id)) {
				logger.info("Inventory with ID of " + inventory.get_id());
				inventoryRepo.deleteById(Id);
				return new ResponseEntity<Inventory>(HttpStatus.NO_CONTENT);
			}

		}
		return new ResponseEntity<Inventory>(HttpStatus.NOT_FOUND);

	}

	// Get inventory by id from inventory collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation("Get Inventory by Id in the system.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Inventory.class) })
	@RequestMapping(value = "/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Inventory> getInventoryById(@PathVariable String Id) {
		List<Inventory> tmp = inventoryRepo.findAll();
		Iterator<Inventory> it = tmp.iterator();
		while (it.hasNext()) {
			Inventory inven = it.next();
			if (inven.get_id().equalsIgnoreCase(Id)) {
				return new ResponseEntity<Inventory>(inven, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Inventory>(HttpStatus.NOT_FOUND);
	}

	// update a record in inventory collection
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{Id}", method = RequestMethod.PUT)
	public ResponseEntity<Inventory> updateInventroy(@PathVariable String Id, @RequestBody Inventory inventory) {

		List<Inventory> inven = inventoryRepo.findAll();
		boolean isValid = validateInventory(inventory);
		if (isValid) {
			Iterator<Inventory> it = inven.iterator();
			while (it.hasNext()) {
				Inventory i = it.next();
				if (i.get_id().equalsIgnoreCase(Id)) {
					if (validateInventory(inventory)) {
						i.setAmount(inventory.getAmount());
						i.setPetsId(inventory.getPetsId());
						i.setType(inventory.getType());
						i.setPrice(inventory.getPrice());
						inventoryRepo.save(i);

						return new ResponseEntity<Inventory>(inventory, HttpStatus.OK);
					}
				}
			}
		}
		return new ResponseEntity<Inventory>(HttpStatus.BAD_REQUEST);

	}

	// validation for PetID and Amount
	public boolean validateInventory(Inventory i) {
		if (i != null && validateControllers.validatePetId(i.getPetsId())
				&& validateControllers.validateAmount(i.getAmount())) {
			return true;
		}
		return false;
	}

}
