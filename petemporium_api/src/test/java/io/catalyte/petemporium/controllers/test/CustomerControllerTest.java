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

import io.catalyte.petemporium.controllers.CustomerController;
import io.catalyte.petemporium.domain.Address;
import io.catalyte.petemporium.domain.Customer;
import io.catalyte.petemporium.domain.Purchases;
import io.catalyte.petemporium.repositories.CustomerRepositorie;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerTest {
	@Mock
	private CustomerRepositorie customerRepo;
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	private Customer customer;
	private Address address;
	private List<Customer> customers;
	private String customerId;
	private String fName = "Norma";
	private String emailID = "norma@gamil.com";
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
		MockitoAnnotations.initMocks(this);
		customer = new Customer();
		address = new Address();
		address.setStreet("23 Hyde Park");
		address.setCity("chicago");
		address.setState("Il");
		address.setZip("60515");
		// customer.set_id(customerId);
		customer.setFirstName(fName);
		customer.setLastName("joe");
		customer.setPhoneNumber("4254355214");
		customer.setEmail(emailID);
		customer.setAddress(address);
		customers = new ArrayList<Customer>();
		customers.add(customer);

		try {
			customerId = createHelper(customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa1POSTCustomerAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(customer);
		MvcResult createCustomerTestResult = mockMvc
				.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(test))
				.andExpect(status().isCreated()).andReturn();
		String content = createCustomerTestResult.getResponse().getContentAsString();
		Map<String, String> expectedResult = new HashMap<String, String>();
		expectedResult.put("firstName", fName);
		expectedResult.put("email", emailID);
		String[] contentList = content.split(",");
		Map<String, String> actualResult = new HashMap<String, String>();

		for (String str : contentList) {
			String[] temp = str.split(":");
			actualResult.put(temp[0].replace("\"", ""), temp[1].replace("\"", ""));

		}

		Assert.assertEquals(expectedResult.get("firstName"), actualResult.get("firstName"));
		Assert.assertEquals(actualResult.get("email"), expectedResult.get("email"));
		logger.debug(content);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa2GetCustomerAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(customer);
		MvcResult getCustomerTestResult = mockMvc
				.perform(get("/customers").contentType(MediaType.APPLICATION_JSON).content(test))
				.andExpect(status().isOk()).andReturn();
		String content = getCustomerTestResult.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa3GetCustomerByIdAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(customer);
		String customerIdLink = "/customers/" + String.valueOf(customerId);
		MvcResult getCustomerTestResultById = mockMvc
				.perform(get(customerIdLink).contentType(MediaType.APPLICATION_JSON).content(test))
				.andExpect(status().isOk()).andReturn();
		String content = getCustomerTestResultById.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa4UpdateCustomerAdmin() throws Exception {

		Gson gson = new Gson();
		String json = gson.toJson(customer);
		String customerIdLink = "/customers/" + String.valueOf(customerId);
		MvcResult result = mockMvc.perform(put(customerIdLink).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testb2DeleteCustomerAdmin() throws Exception {
		// mockMvc.perform(delete("/customers/10").contentType(MediaType.APPLICATION_JSON))

		// .andExpect(status().isNoContent());
		String customerIdLink = "/customers/" + String.valueOf(customerId);
		mockMvc.perform(delete(customerIdLink).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	// USER role testing
	@Test
	@WithMockUser(username = "user1", roles = { "USER" })
	public void testa5AddCustomerUser() throws Exception {
		try {
			Gson gson = new Gson();
			String test = gson.toJson(customer);
			mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(test))
					.andExpect(status().isForbidden()).andReturn();
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa6GetCustomerUser() throws Exception {
		this.mockMvc.perform(get("/customers")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa7GetCustomerByIdUser() throws Exception {
		this.mockMvc.perform(get("/customers/11")).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa8EditCustomerUser() throws Exception {

		Gson gson = new Gson();
		String json = gson.toJson(customer);
		String customerIdLink = "/customers/" + String.valueOf(customerId);
		MvcResult result = mockMvc.perform(put(customerIdLink).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isForbidden()).andReturn();

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testb3DeleteCustomerUser() throws Exception {

		try {
			String customerIdLink = "/customers/" + String.valueOf(customerId);
			mockMvc.perform(delete(customerIdLink).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isForbidden());
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}

	}

	// method get Id from a post and set a Id for another test method
	public String createHelper(Customer c) throws Exception {
		try {
			Gson gson = new Gson();
			String test = gson.toJson(c);
			MvcResult customerResult = mockMvc
			.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(test)).andReturn();
			String content = customerResult.getResponse().getContentAsString();
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
