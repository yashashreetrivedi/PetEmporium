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

import io.catalyte.petemporium.controllers.PetController;

import io.catalyte.petemporium.domain.Pet;

import io.catalyte.petemporium.repositories.PetRepositorie;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PetControllerTest {
	@Mock
	private PetRepositorie petRepo;
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
	private Pet pet;
	private List<Pet> pets;
	private String petId;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
		MockitoAnnotations.initMocks(this);
		pet = new Pet();
		pet.setName("Dog");
		pet.setSex("M");
		pet.setAge("2");
		pet.setColor("Brown");
		pets = new ArrayList<Pet>();
		pets.add(pet);

		try {
			petId = createHelper(pet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa1POSTPetAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(pet);
		MvcResult createPetTestResult = mockMvc
				.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(test))
				.andExpect(status().isCreated()).andReturn();
		String content = createPetTestResult.getResponse().getContentAsString();

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa2GetPetAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(pet);
		MvcResult getPetTestResult = mockMvc.perform(get("/pets").contentType(MediaType.APPLICATION_JSON).content(test))
				.andExpect(status().isOk()).andReturn();
		String content = getPetTestResult.getResponse().getContentAsString();

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa3GetPetByIdAdmin() throws Exception {
		Gson gson = new Gson();
		String test = gson.toJson(pet);
		String petIdLink = "/pets/" + String.valueOf(petId);
		MvcResult getCustomerTestResultById = mockMvc
				.perform(get(petIdLink).contentType(MediaType.APPLICATION_JSON).content(test))
				.andExpect(status().isOk()).andReturn();
		String content = getCustomerTestResultById.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testa4UpdatePetAdmin() throws Exception {

		Gson gson = new Gson();
		String json = gson.toJson(pet);
		String petIdLink = "/pets/" + String.valueOf(petId);
		MvcResult result = mockMvc.perform(put(petIdLink).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testb2DeletePetAdmin() throws Exception {

		String petIdLink = "/pets/" + String.valueOf(petId);
		mockMvc.perform(delete(petIdLink).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	// User role testing
	@Test
	@WithMockUser(username = "user1", roles = { "USER" })
	public void testa5createPetUser() throws Exception {
		try {
			Gson gson = new Gson();
			String test = gson.toJson(pet);
			mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(test))
					.andExpect(status().isForbidden()).andReturn();
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa6GetPetUser() throws Exception {
		this.mockMvc.perform(get("/pets")).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa7GetPetByIdUser() throws Exception {
		this.mockMvc.perform(get("/pets/AA02")).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testa8EditPetUser() throws Exception {
		try {

			mockMvc.perform(delete("/pets/petId").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isForbidden());
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}

	}

	@Test
	@WithMockUser(roles = "USER")
	public void testb3DeletePetUser() throws Exception {
		try {
			String petIdLink = "/pets/" + String.valueOf(petId);
			mockMvc.perform(delete(petIdLink).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isForbidden());
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
		}

	}

	// create method for Id
	public String createHelper(Pet p) throws Exception {
		try {
			Gson gson = new Gson();
			String test = gson.toJson(p);
			MvcResult petResult = mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(test))
					.andReturn();
			String content = petResult.getResponse().getContentAsString();
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
