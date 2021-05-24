package com.java.assessment.clientmemberapp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.assessment.clientmemberapp.model.Client;
import com.java.assessment.clientmemberapp.model.ClientSearch;
import com.java.assessment.clientmemberapp.service.ClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * Client REST Controller
 *
 * @author Sowjanya Gonuguntla
 */

@RestController
@Api(tags = { "Client Controller" })
@SwaggerDefinition(tags = { @Tag(name = "Client Controller", description = "Client Member App REST API") })
public class ClientController {

	@Autowired
	ClientService clientService;

	/**
	 * Create Clients.
	 *
	 * @param Client the Client
	 * @return the created Client
	 */
	@PostMapping("/create")
	@ApiOperation(value = "Create a new Client with all mandatory fileds", nickname = "saveClient")
	public Client saveClient(@Valid @RequestBody Client client) {
		return clientService.insertClient(client);
	}

	/**
	 * Create List of Clients
	 *
	 * @param Clients the Clients list
	 * @return the list of created Clients
	 */

	@PostMapping("/createListOfClients")
	@ApiOperation(value = "Create a list of new Client with all mandatory fileds", nickname = "saveClientList")
	public List<Client> saveClientList(@Valid @RequestBody List<Client> clients) {
		return clientService.insertListOfClients(clients);
	}

	/**
	 * update the client
	 *
	 * @param Client the Client
	 * @return the updated Client
	 */

	@PutMapping("/update")
	@ApiOperation(value = "Update client with input fileds of Client class", nickname = "udateClient")
	public Optional<Client> udateClient(@RequestBody Client client) {
		return clientService.updateByInput(client);

	}

	/**
	 * search the clients by first name , last name and mobile number.
	 *
	 * @param ClientSearch the search
	 * @return the list of Clients
	 */

	@PostMapping("/search")
	@ApiOperation(value = "Search client with first Name, Last Name and Mobile Number", nickname = "getSerachResults")
	public List<Client> getSerachResults(@RequestBody ClientSearch search) {
		return clientService.fetchClientByCriteria(search);
	}

	/**
	 * Get all Clients
	 *
	 * @return the list of Clients
	 */

	@GetMapping("/getClients")
	@ApiOperation(value = "Get all existed clients in Database", nickname = "getClients")
	public List<Client> getClients() {
		return clientService.fetchAllClients();

	}

	/**
	 * Get client details by id.
	 *
	 * @param id the Client.id
	 * @return the client details
	 */

	@GetMapping("/getClientById/{id}")
	@ApiOperation(value = "Get client data by ID", nickname = "getClientByID")
	public Optional<Client> getClientByID(@PathVariable(value = "id") Long id) {
		return clientService.fetchClientById(id);

	}

}
