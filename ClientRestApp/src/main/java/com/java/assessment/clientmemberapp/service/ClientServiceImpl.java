package com.java.assessment.clientmemberapp.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.ClientType;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.java.assessment.clientmemberapp.exception.ClientDataValidationException;
import com.java.assessment.clientmemberapp.exception.ClientNotFoundException;
import com.java.assessment.clientmemberapp.exception.DuplicateValuesException;
import com.java.assessment.clientmemberapp.model.Client;
import com.java.assessment.clientmemberapp.model.ClientSearch;
import com.java.assessment.clientmemberapp.repository.ClientRepository;

/**
 * The Client service implementations.
 *
 * @author Sowjanya Gonuguntla
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Update the Client on Criteria basis
	 *
	 * @param Client the Client
	 * @return the Optional
	 */

	@Override
	public Optional<Client> updateByInput(Client client) {
		validateClient(client);
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		entityManager
				.createQuery(
						buildUpdateQuery(client, criteriaBuilder, criteriaBuilder.createCriteriaUpdate(Client.class)))
				.executeUpdate();
		return clientRepository.findById(client.getId());
	}

	private CriteriaUpdate<Client> buildUpdateQuery(Client client, CriteriaBuilder criteriaBuilder,
			CriteriaUpdate<Client> updateQuery) {
		Root<Client> clientRoot = updateQuery.from(Client.class);
		updateQuery = (checkInputString(client.getIdNumber()))
				? updateQuery.set(clientRoot.get("idNumber"), client.getIdNumber())
				: updateQuery;
		updateQuery = (checkInputString(client.getMobileNumber()))
				? updateQuery.set(clientRoot.get("mobileNumber"), client.getMobileNumber())
				: updateQuery;
		updateQuery = (checkInputString(client.getFirstName()))
				? updateQuery.set(clientRoot.get("firstName"), client.getFirstName())
				: updateQuery;
		updateQuery = (checkInputString(client.getLastName()))
				? updateQuery.set(clientRoot.get("lastName"), client.getLastName())
				: updateQuery;
		updateQuery = (checkInputString(client.getAddress()))
				? updateQuery.set(clientRoot.get("address"), client.getAddress())
				: updateQuery;

		updateQuery.where(criteriaBuilder.equal(clientRoot.get("id"), client.getId()));
		return updateQuery;
	}

	/**
	 * Insert Client
	 *
	 * @param Client the Client
	 * @return the Client
	 */
	@Override
	public Client insertClient(Client client) {
		Client insertedClient = null;
		try {
			insertedClient = clientRepository.save(validateClient(client));
		} catch (InvalidDataAccessApiUsageException ex) {
			throw new ClientDataValidationException("Please provide all mandatory values in the request");
		} catch (ConstraintViolationException ex) {
			throw new ClientNotFoundException("Client record cration failed");
		}
		return insertedClient;
	}

	/**
	 * Find by Client id
	 *
	 * @param id the Client id
	 * @return the optional
	 */
	@Override
	public Optional<Client> fetchClientById(Long id) {
		Optional<Client> client = clientRepository.findById(id);
		if (!client.isPresent())
			throw new ClientNotFoundException("No data found with the provided client ID : " + id);
		return client;
	}

	/**
	 * Find Client by criteria
	 *
	 * @param the ClientSearch search
	 * @return the list of clients
	 */
	@Override
	public List<Client> fetchClientByCriteria(ClientSearch search) {
		return clientRepository.findBy(search);
	}

	/**
	 * Find all Clients
	 *
	 * @return the list of clients
	 */
	@Override
	public List<Client> fetchAllClients() {
		return clientRepository.findAll();

	}

	/**
	 * Update the Client
	 *
	 * @param Client the CLient
	 * @return the Client
	 */
	@Override
	public Client updateClient(Client client) {
		return clientRepository.saveAndFlush(validateClient(client));
	}

	/**
	 * Add the list of Client
	 *
	 * @param Clients the list of Clients
	 * @return the list of Clients
	 */
	@Override
	public List<Client> insertListOfClients(List<Client> clientList) {
		return clientRepository.saveAll(clientList);

	}

	/**
	 * Validate the Client data for SA ID number and mandatory checks
	 *
	 * @param Client the Client
	 * @return the Client
	 */
	private Client validateClient(Client client) {
		if (client.getIdNumber() != null && client.getIdNumber().length() == 13) {
			DateTimeFormatter dtfIn = DateTimeFormatter.ofPattern("yyMMdd");
			String dob = client.getIdNumber().substring(0, 6);
			try {
				LocalDate ld = LocalDate.parse(dob, dtfIn);
				LocalDate idDate = ld.isAfter(LocalDate.now()) ? ld.minusYears(100) : ld;
				System.out.println(idDate);
			} catch (DateTimeParseException e) {
				throw new ClientDataValidationException(
						"ID number has invalid Date of Birth , It should be in yyMMdd format of first six digits");
			}
			Integer genderId = Integer.parseInt(client.getIdNumber().substring(10, 11));
			if (!(genderId == 1 || genderId == 0 ? true : false)) {
				throw new ClientDataValidationException("11th Digit should be 0 or 1, as it represents Citizenship");
			}
		} else if (client.getIdNumber() != null && client.getIdNumber().length() != 13) {

			throw new ClientDataValidationException("ID number must be 13 digits");

		}

		if (clientRepository.findByIdNumber(client.getIdNumber()).size() > 0) {
			throw new DuplicateValuesException("ID number already mapped with other records , Please correct");
		}
		if (clientRepository.findByMobileNumber(client.getMobileNumber()).size() > 0) {
			throw new DuplicateValuesException("Mobile number already mapped with other records , Please correct");
		}
		return client;
	}

	/**
	 * Check input String null and empty check
	 *
	 * @param input String
	 * @return boolean value
	 */
	private boolean checkInputString(String input) {
		if (input != null && !input.trim().isEmpty()) {
			return true;
		}
		return false;
	}

}
