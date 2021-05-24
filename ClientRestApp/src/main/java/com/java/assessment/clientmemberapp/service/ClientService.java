package com.java.assessment.clientmemberapp.service;

import java.util.List;
import java.util.Optional;

import com.java.assessment.clientmemberapp.model.Client;
import com.java.assessment.clientmemberapp.model.ClientSearch;

/**
 * The interface Client service.
 */
public interface ClientService {

	public Client insertClient(Client client);

	public List<Client> insertListOfClients(List<Client> clientList);

	public Optional<Client> fetchClientById(Long id);

	public List<Client> fetchClientByCriteria(ClientSearch search);

	public List<Client> fetchAllClients();

	public Client updateClient(Client client);

	public Optional<Client> updateByInput(Client client);

}
