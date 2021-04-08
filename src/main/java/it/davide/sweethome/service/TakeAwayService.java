package it.davide.sweethome.service;

import it.davide.sweethome.domain.TakeAway;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TakeAway}.
 */
public interface TakeAwayService {
    /**
     * Save a takeAway.
     *
     * @param takeAway the entity to save.
     * @return the persisted entity.
     */
    TakeAway save(TakeAway takeAway);

    /**
     * Partially updates a takeAway.
     *
     * @param takeAway the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TakeAway> partialUpdate(TakeAway takeAway);

    /**
     * Get all the takeAways.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TakeAway> findAll(Pageable pageable);

    /**
     * Get the "id" takeAway.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TakeAway> findOne(Long id);

    /**
     * Delete the "id" takeAway.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the takeAway corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TakeAway> search(String query, Pageable pageable);
}
