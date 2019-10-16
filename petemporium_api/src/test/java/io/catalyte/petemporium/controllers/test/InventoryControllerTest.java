package io.catalyte.petemporium.controllers.test;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import io.catalyte.petemporium.controllers.InventoryController;
import io.catalyte.petemporium.domain.Inventory;
import io.catalyte.petemporium.domain.Pet;
import io.catalyte.petemporium.repositories.InventoryRepositorie;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InventoryControllerTest {
	@Mock
	private InventoryRepositorie inventoryRepo;
	private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
	private Inventory inventory;
	private List<Inventory> inventories;
	private String inventoryId="Inven014";
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;
	@Before
	public void setUp()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
		MockitoAnnotations.initMocks(this);
		inventory = new Inventory();
		inventory.set_id(inventoryId);
		inventory.setPetsId("AAp01");
		inventory.setAmount(5.00);
		inventory.setType("cat");
		inventory.setPrice(100.00);
		inventories = new ArrayList<Inventory>();
		inventories.add(inventory);
	}
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa1POSTInventoryAdmin() throws Exception {
	Gson gson = new Gson();
	String test = gson.toJson(inventory);
	MvcResult createInventoryTestResult = mockMvc.perform(post("/inventory").contentType(MediaType.APPLICATION_JSON).content(test))
	.andExpect(status().isCreated()).andReturn();
	String content = createInventoryTestResult .getResponse().getContentAsString();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa2GetInventoryAdmin() throws Exception {

		
		Gson gson = new Gson();
		String test = gson.toJson(inventory);
		MvcResult getCustomerTestResult = mockMvc
		.perform(get("/inventory").contentType(MediaType.APPLICATION_JSON).content(test))
		.andExpect(status().isOk()).andReturn();
		String content = getCustomerTestResult.getResponse().getContentAsString();
     }
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa3GetInventoryByIdAdmin() throws Exception {

		Gson gson = new Gson();
		String test = gson.toJson(inventory);
		MvcResult getInventoryTestResultById = mockMvc.perform(get("/inventory/inven005").contentType(MediaType.APPLICATION_JSON).content(test))
		.andExpect(status().isOk()).andReturn();
		String content = getInventoryTestResultById .getResponse().getContentAsString();

		
    }
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa4UpdateInventoryAdmin() throws Exception {
    Gson gson = new Gson();
	String json = gson.toJson(inventory);
	MvcResult result = mockMvc.perform(
	put("/inventory/inven006").contentType(MediaType.APPLICATION_JSON).content(json))
	.andExpect(status().isOk()).andReturn();
	String content = result.getResponse().getContentAsString();
		

	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testb2DeleteInventoryAdmin() throws Exception {
    mockMvc.perform(delete("/Inventory/inventroyId").contentType(MediaType.APPLICATION_JSON))
	.andExpect(status().isNoContent());
		
	}

	// USER role testing
	@Test
	@WithMockUser(username = "user1", roles = { "USER" })
	public void testa5AddInventoryUser() throws Exception {
	try {
	Gson gson = new Gson();
	String test = gson.toJson(inventory);
	mockMvc.perform(post("/inventory").contentType(MediaType.APPLICATION_JSON).content(test))
	.andExpect(status().isForbidden()).andReturn();
    } catch (Exception e) {
	Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
	}
    }

	@Test
	@WithMockUser(roles = "USER")
	public void testa6GetinventoryUser() throws Exception {
	this.mockMvc.perform(get("/inventory")).andExpect(status().isOk());
    }

	@Test
	@WithMockUser(roles = "USER")
	public void testa7EditInventoryUser() throws Exception {
		try {

		mockMvc.perform(delete("/inventroy/inven003").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}
    }

	
	@Test
	@WithMockUser(roles = "USER")
	public void testb3DeleteInventoryUser() throws Exception {
	try {
     mockMvc.perform(delete("/inventory/inventoryId").contentType(MediaType.APPLICATION_JSON))
	  .andExpect(status().isForbidden());
	} catch (Exception e) {
	Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
	}

	}
	
	
	

}
