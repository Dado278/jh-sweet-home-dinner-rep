package it.davide.sweethome.service;

import it.davide.sweethome.domain.Innkeeper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Innkeeper}.
 */
public interface InnkeeperService {

    /**
     * Save a innkeeper.
     *
     * @param innkeeper the entity to save.
     * @return the persisted entity.
     */
    Innkeeper save(Innkeeper innkeeper);

    /**
     * Get all the innkeepers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Innkeeper> findAll(Pageable pageable);


    /**
     * Get the "id" innkeeper.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Innkeeper> findOne(Long id);

    /**
     * Delete the "id" innkeeper.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the innkeeper corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Innkeeper> search(String query, Pageable pageable);
}
