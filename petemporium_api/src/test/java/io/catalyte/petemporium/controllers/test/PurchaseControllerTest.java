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
import java.util.regex.Pattern;

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

import io.catalyte.petemporium.controllers.PurchaseController;
import io.catalyte.petemporium.domain.Address;
import io.catalyte.petemporium.domain.Customer;
import io.catalyte.petemporium.domain.Pet;
import io.catalyte.petemporium.domain.PurchaseItem;
import io.catalyte.petemporium.domain.Purchases;
import io.catalyte.petemporium.repositories.PurchaseRepositorie;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PurchaseControllerTest {
	@Mock
	private PurchaseRepositorie purchaseRepo;
	private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	private Purchases purchase;
	private PurchaseItem item;
	private Customer customer;
	private Pet pet;
	private Address address;

	private List<Purchases> purchases;
	private List<PurchaseItem> items;
	// private String purchaseId="109";
	private String purchId;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;

	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
		MockitoAnnotations.initMocks(this);
		purchase = new Purchases();
		item = new PurchaseItem();
		customer = new Customer();
		pet = new Pet();
		address = new Address();
        address.setStreet("23 Hyde Park");
		address.setCity("chicago");
		address.setState("Il");
		address.setZip("60515");
		purchase.setDate("02/22/2017");
		purchase.setTotalPrice(100.00);
		customer.set_id("1");
		customer.setFirstName("Eliza");
		customer.setLastName("Bell");
		customer.setPhoneNumber("4354456789");
		customer.setEmail("eliza@gmail.com");
		customer.setAddress(address);
        pet.set_id("pet1");
		pet.setName("SuperDog");
		pet.setAge("2");
		pet.setColor("Black");
		pet.setSex("M");
		item.setItem_Id("I001");
		item.setPrice(90.00);
		item.setPet(pet);
		purchase.setCustomer(customer);
		items = new ArrayList<PurchaseItem>();
		purchase.setItems(items);
		purchases = new ArrayList<Purchases>();
		purchases.add(purchase);

		try {
			purchId = createHelper(purchase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa1POSTPurchaseAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(purchase);
		MvcResult createTestResult = mockMvc
		.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(test))
		.andExpect(status().isCreated()).andReturn();
		String content = createTestResult.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa2GetPurchaseAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(purchase);
		MvcResult getTestResult = mockMvc
		.perform(get("/purchases").contentType(MediaType.APPLICATION_JSON).content(test))
		.andExpect(status().isOk()).andReturn();
		String content = getTestResult.getResponse().getContentAsString();

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa3GetPurchaseByIdAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(purchase);
		String purchIdLink = "/purchases/" + String.valueOf(purchId);
		MvcResult getTestResultById = mockMvc
	    .perform(get(purchIdLink).contentType(MediaType.APPLICATION_JSON).content(test))
		.andExpect(status().isOk()).andReturn();
		String content = getTestResultById.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa4UpdatePurchaseAdmin() throws Exception {
		Gson gson = new Gson();
		String json = gson.toJson(purchase);
		String purchIdLink = "/purchases/" + String.valueOf(purchId);
		MvcResult result = mockMvc.perform(put(purchIdLink).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testb2DeletePurchaseAdmin() throws Exception {
		String purchIdLink = "/purchases/" + String.valueOf(purchId);
		mockMvc.perform(delete(purchIdLink).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	// USER role testing
	@Test
	@WithMockUser(username = "user1", roles = { "USER" })
	public void testa5AddPurchaseUser() throws Exception {
		try {
			Gson gson = new Gson();
			String test = gson.toJson(purchase);
			mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(test))
		   .andExpect(status().isForbidden()).andReturn();
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa6GetPurchaseUser() throws Exception {
		this.mockMvc.perform(get("/purchases")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa7GetPurchaseByIdUser() throws Exception {
		this.mockMvc.perform(get("/purchases/AA02")).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa7EditPurchaseUser() throws Exception {
		
		Gson gson = new Gson();
		String json = gson.toJson(purchase);
		String purchaseIdLink = "/purchases/" + String.valueOf(purchId);
		MvcResult result = mockMvc.perform(put(purchaseIdLink).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isForbidden()).andReturn();

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testb3DeletePurchaseUser() throws Exception {
		
		try {
			String idLink = "/purchases/" + String.valueOf(purchId);
			mockMvc.perform(delete(idLink).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}
	}
//create a helper metohs for ID 
	public String createHelper(Purchases p) throws Exception {
		try {
			Gson gson = new Gson();
			String test = gson.toJson(p);
			MvcResult purchaseResult = mockMvc
					.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(test)).andReturn();

			String content = purchaseResult.getResponse().getContentAsString();

			String[] contentList = content.split(",");

			Map<String, String> actualResult = new HashMap<String, String>();

			for (String str : contentList) {
				String[] temp = str.split(":");

				temp[0].replace("\"", "");
				String t = temp[0].replaceAll("[\\{\\}\\(\\)]", "");
				Boolean checkId = t.startsWith("\"_id");

				if (checkId) {
					return temp[1].replace("\"", "");
				}

			}

			return "id not found";
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

}
