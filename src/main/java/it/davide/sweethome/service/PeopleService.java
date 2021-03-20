package it.davide.sweethome.service;

import it.davide.sweethome.domain.People;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link People}.
 */
public interface PeopleService {

    /**
     * Save a people.
     *
     * @param people the entity to save.
     * @return the persisted entity.
     */
    People save(People people);

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<People> findAll(Pageable pageable);


    /**
     * Get the "id" people.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<People> findOne(Long id);

    /**
     * Delete the "id" people.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the people corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<People> search(String query, Pageable pageable);
}
