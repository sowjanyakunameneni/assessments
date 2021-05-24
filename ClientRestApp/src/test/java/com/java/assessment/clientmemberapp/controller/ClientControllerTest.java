package com.java.assessment.clientmemberapp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.reflect.TypeToken;
import com.java.assessment.clientmemberapp.model.Client;
import com.java.assessment.clientmemberapp.model.ClientSearch;
import com.java.assessment.clientmemberapp.repository.ClientRepository;
import com.java.assessment.clientmemberapp.service.ClientService;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ClientService clientService;

	@MockBean
	ClientRepository clientRepository;

	Optional<Client> mockClient = Optional
			.ofNullable(new Client(1L, "Sowjanya", "Gonuguntla", "9898989898", "1203252654089", "Pune, MH"));
	Optional<Client> mockClientEmpty = Optional.ofNullable(new Client());

	@Test
	public void shouldReturnNotFoundWhenNotRequestingToCorrectPath() throws Exception {
		mockMvc.perform(get("/invalid/Path").contentType(MediaType.TEXT_PLAIN)).andExpect(status().isNotFound());
	}

	@Test
	public void getClientById() throws Exception {

		when(clientService.fetchClientById(Mockito.anyLong())).thenReturn(mockClient);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/getClientById/1").accept(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		Client responseClient = TestUtils.jsonToObject(result.getResponse().getContentAsString(), Client.class);

		assertNotNull(result);
		assertEquals(1l, responseClient.getId().longValue());
		assertEquals("Sowjanya", responseClient.getFirstName());
	}

	@Test
	public void getClientById_Client_NotFound() throws Exception {

		when(clientService.fetchClientById(Mockito.anyLong())).thenReturn(mockClientEmpty);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/getClientById/2").accept(MediaType.APPLICATION_JSON)).andReturn();
		Client responseClient = TestUtils.jsonToObject(result.getResponse().getContentAsString(), Client.class);

		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertNotNull(result);
		assertEquals(null, responseClient.getId());

	}

	@Test
	public void createClient() throws Exception {

		Client clientOne = new Client(1L, "Sachin", "Tendulkar", "9897898989", "5612035465089", "Mumbai");

		when(clientService.insertClient(Mockito.any(Client.class))).thenReturn(clientOne);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(clientOne)).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		Client responseClient = TestUtils.jsonToObject(response.getContentAsString(), Client.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(1l, responseClient.getId().longValue());
		assertEquals("Sachin", responseClient.getFirstName());

	}

	@Test
	public void updateClient() throws Exception {

		Client clientOne = new Client(1L, "SachinRamesh", "Tendulkar", "9897898989", "5612035465089", "Mumbai");

		when(clientService.insertClient(Mockito.any(Client.class))).thenReturn(clientOne);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/update").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(clientOne)).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	@Test
	public void createClient_Invalid_Mobile_Number() throws Exception {
		Client clientOne = new Client(1L, "Sachin", "Tendulkar", "989798989", "5612035465089", "Mumbai");

		when(clientService.insertClient(Mockito.any(Client.class))).thenReturn(clientOne);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(clientOne)).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		Client responseClient = TestUtils.jsonToObject(response.getContentAsString(), Client.class);

		assertEquals(400, response.getStatus());
		assertEquals("Mobile number should be 10 digits", responseClient.getMobileNumber());

	}

	@Test
	public void createClient_Invalid_FirstANDLastName() throws Exception {
		Client clientOne = new Client(1L, "5612035465089", "Mumbai");

		when(clientService.insertClient(Mockito.any(Client.class))).thenReturn(clientOne);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(clientOne)).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		Client responseClient = TestUtils.jsonToObject(response.getContentAsString(), Client.class);

		assertEquals(400, response.getStatus());
		assertEquals("First Name is mandatory", responseClient.getFirstName());
		assertEquals("Last Name is mandatory", responseClient.getLastName());

	}

	@Test
	public void createClient_with_ListOfClients() throws Exception {

		when(clientService.insertListOfClients(Mockito.anyList())).thenReturn(TestUtils.buildClients());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createListOfClients")
				.accept(MediaType.APPLICATION_JSON).content(TestUtils.objectToJson(TestUtils.buildClients()))
				.contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(2, TestUtils.jsonToList(response.getContentAsString(), new TypeToken<ArrayList<Client>>() {
		}).size());
	}

	@Test
	public void getListOfClients() throws Exception {

		when(clientService.fetchAllClients()).thenReturn(TestUtils.buildClients());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getClients").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(TestUtils.buildClients())).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(2, TestUtils.jsonToList(response.getContentAsString(), new TypeToken<ArrayList<Client>>() {
		}).size());
	}

	@Test
	public void serachClients() throws Exception {

		ClientSearch search = new ClientSearch();
		search.setFirstName("Rohith");

		when(clientService.fetchClientByCriteria(Mockito.any(ClientSearch.class))).thenReturn(TestUtils.buildClients());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/search").accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(search)).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(200, response.getStatus());

	}

}
