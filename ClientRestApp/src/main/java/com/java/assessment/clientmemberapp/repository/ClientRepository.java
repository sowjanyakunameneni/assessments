package com.java.assessment.clientmemberapp.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.java.assessment.clientmemberapp.model.Client;
import com.java.assessment.clientmemberapp.model.ClientSearch;

/**
 * The interface Client repository.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

	List<Client> findByIdNumber(String idNumber);

	List<Client> findByMobileNumber(String mobileNumber);

	/**
	 * Find by ClientSearch.
	 *
	 * @param search the Clients by given criteria
	 * @return the list of Clients
	 */

	default List<Client> findBy(ClientSearch search) {

		return findAll((root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();
			if (search.getFirstName() != null) {
				predicates.add(cb.equal(root.<Long>get("firstName"), search.getFirstName()));
			}
			if (search.getLastName() != null) {
				predicates.add(cb.equal(root.<String>get("lastName"), search.getLastName()));
			}
			if (search.getMobileNumber() != null) {
				predicates.add(cb.equal(root.<String>get("mobileNumber"), search.getMobileNumber()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		});
	}

}
