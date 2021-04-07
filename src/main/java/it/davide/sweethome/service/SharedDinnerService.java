package it.davide.sweethome.service;

import it.davide.sweethome.domain.SharedDinner;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link SharedDinner}.
 */
public interface SharedDinnerService {
    /**
     * Save a sharedDinner.
     *
     * @param sharedDinner the entity to save.
     * @return the persisted entity.
     */
    SharedDinner save(SharedDinner sharedDinner);

    /**
     * Partially updates a sharedDinner.
     *
     * @param sharedDinner the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SharedDinner> partialUpdate(SharedDinner sharedDinner);

    /**
     * Get all the sharedDinners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SharedDinner> findAll(Pageable pageable);

    /**
     * Get the "id" sharedDinner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SharedDinner> findOne(Long id);

    /**
     * Delete the "id" sharedDinner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the sharedDinner corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SharedDinner> search(String query, Pageable pageable);
}
