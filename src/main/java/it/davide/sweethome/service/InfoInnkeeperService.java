package it.davide.sweethome.service;

import it.davide.sweethome.domain.InfoInnkeeper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link InfoInnkeeper}.
 */
public interface InfoInnkeeperService {

    /**
     * Save a infoInnkeeper.
     *
     * @param infoInnkeeper the entity to save.
     * @return the persisted entity.
     */
    InfoInnkeeper save(InfoInnkeeper infoInnkeeper);

    /**
     * Get all the infoInnkeepers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InfoInnkeeper> findAll(Pageable pageable);


    /**
     * Get the "id" infoInnkeeper.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InfoInnkeeper> findOne(Long id);

    /**
     * Delete the "id" infoInnkeeper.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the infoInnkeeper corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InfoInnkeeper> search(String query, Pageable pageable);
}
