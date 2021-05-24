package com.java.assessment.clientmemberapp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.reflect.TypeToken;
import com.java.assessment.clientmemberapp.controller.TestUtils;
import com.java.assessment.clientmemberapp.model.Client;
import com.java.assessment.clientmemberapp.model.ClientSearch;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ClientRestAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ClientRestAppApplicationIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void createClientSuccessTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "9897912989", "2501122366089", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Sachin"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("9897912989"));
	}

	@Test
	public void createClientwithInvalidSAID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "9897982989", "5659898989887", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(
						"ID number has invalid Date of Birth , It should be in yyMMdd format of first six digits"));

	}

	@Test
	public void createClient_Invalid_Mobile_Number() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "989798989", "5659898989887", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("Mobile number should be 10 digits"));

	}

	@Test
	public void createClientFailureTest_without_Mandatory_fileds() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(new Client("6565656565", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("First Name is mandatory"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Last Name is mandatory"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.idNumber").value("Valid ID Number is mandatory"));

	}

	@Test
	public void createClient_with_Duplicate_ID_Number_Test() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "9897968989", "5601128989187", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Sachin"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("9897968989"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.idNumber").value("5601128989187"));

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(
						TestUtils.objectToJson(new Client("Rohtih", "Sharma", "9895898989", "5601128989187", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message")
						.value("ID number already mapped with other records , Please correct"));

	}

	@Test
	public void createClient_with_Invalid_IDNumber_length() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "9897989989", "56011289189", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ID number must be 13 digits"));

	}

	@Test
	public void createClientFailureTest_Invalid_Gender_Digit_in_Id_Number() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "9897989989", "5601128989287", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message")
						.value("11th Digit should be 0 or 1, as it represents Citizenship"));

	}

	@Test
	public void updateClient() throws Exception {

		Client clientToUpdate = new Client();

		MvcResult resultOne = mockMvc.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "9897998989", "5601128989186", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andReturn();
		clientToUpdate
				.setId(TestUtils.jsonToObject(resultOne.getResponse().getContentAsString(), Client.class).getId());
		clientToUpdate.setFirstName("SachinRamesh");

		mockMvc.perform(MockMvcRequestBuilders.put("/update").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(clientToUpdate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("SachinRamesh"));

	}

	@Test
	public void serachClientTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/createListOfClients").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(TestUtils.buildClients())).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		ClientSearch search = new ClientSearch();
		search.setLastName("Sharma");
		MvcResult results = mockMvc.perform(MockMvcRequestBuilders.post("/search").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(search)).contentType(MediaType.APPLICATION_JSON)).andReturn();
		@SuppressWarnings("unchecked")
		List<Client> serchOutput = TestUtils.jsonToList(results.getResponse().getContentAsString(),
				new TypeToken<ArrayList<Client>>() {
				});
		assertEquals(1, serchOutput.size());
		assertEquals("Rohith", serchOutput.get(0).getFirstName());

	}

	@Test
	public void getByClientId() throws Exception {

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
						.content(TestUtils.objectToJson(
								new Client("Sachin", "Tendulkar", "9897982989", "2501122356089", "Mumbai")))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Sachin"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("9897982989")).andReturn();
		Client client = TestUtils.jsonToObject(result.getResponse().getContentAsString(), Client.class);
		mockMvc.perform(MockMvcRequestBuilders.get("/getClientById/" + client.getId()).accept(MediaType.APPLICATION_JSON)
				.content(TestUtils
						.objectToJson(new Client("Sachin", "Tendulkar", "9897982989", "2501122356089", "Mumbai")))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Sachin"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("9897982989")).andReturn();

	}

}
