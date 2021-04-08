package it.davide.sweethome.service;

import it.davide.sweethome.domain.Customer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Customer}.
 */
public interface CustomerService {
    /**
     * Save a customer.
     *
     * @param customer the entity to save.
     * @return the persisted entity.
     */
    Customer save(Customer customer);

    /**
     * Partially updates a customer.
     *
     * @param customer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Customer> partialUpdate(Customer customer);

    /**
     * Get all the customers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Customer> findAll(Pageable pageable);

    /**
     * Get all the customers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Customer> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" customer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Customer> findOne(Long id);

    /**
     * Delete the "id" customer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the customer corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Customer> search(String query, Pageable pageable);
}
