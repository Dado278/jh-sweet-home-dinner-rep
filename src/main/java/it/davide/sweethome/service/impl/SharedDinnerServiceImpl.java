package it.davide.sweethome.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import it.davide.sweethome.domain.SharedDinner;
import it.davide.sweethome.repository.SharedDinnerRepository;
import it.davide.sweethome.repository.search.SharedDinnerSearchRepository;
import it.davide.sweethome.service.SharedDinnerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SharedDinner}.
 */
@Service
@Transactional
public class SharedDinnerServiceImpl implements SharedDinnerService {

    private final Logger log = LoggerFactory.getLogger(SharedDinnerServiceImpl.class);

    private final SharedDinnerRepository sharedDinnerRepository;

    private final SharedDinnerSearchRepository sharedDinnerSearchRepository;

    public SharedDinnerServiceImpl(
        SharedDinnerRepository sharedDinnerRepository,
        SharedDinnerSearchRepository sharedDinnerSearchRepository
    ) {
        this.sharedDinnerRepository = sharedDinnerRepository;
        this.sharedDinnerSearchRepository = sharedDinnerSearchRepository;
    }

    @Override
    public SharedDinner save(SharedDinner sharedDinner) {
        log.debug("Request to save SharedDinner : {}", sharedDinner);
        SharedDinner result = sharedDinnerRepository.save(sharedDinner);
        sharedDinnerSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<SharedDinner> partialUpdate(SharedDinner sharedDinner) {
        log.debug("Request to partially update SharedDinner : {}", sharedDinner);

        return sharedDinnerRepository
            .findById(sharedDinner.getId())
            .map(
                existingSharedDinner -> {
                    if (sharedDinner.getCreateDate() != null) {
                        existingSharedDinner.setCreateDate(sharedDinner.getCreateDate());
                    }
                    if (sharedDinner.getUpdateDate() != null) {
                        existingSharedDinner.setUpdateDate(sharedDinner.getUpdateDate());
                    }
                    if (sharedDinner.getTitle() != null) {
                        existingSharedDinner.setTitle(sharedDinner.getTitle());
                    }
                    if (sharedDinner.getSlogan() != null) {
                        existingSharedDinner.setSlogan(sharedDinner.getSlogan());
                    }
                    if (sharedDinner.getDescription() != null) {
                        existingSharedDinner.setDescription(sharedDinner.getDescription());
                    }
                    if (sharedDinner.getDinnerDate() != null) {
                        existingSharedDinner.setDinnerDate(sharedDinner.getDinnerDate());
                    }
                    if (sharedDinner.getHomePage() != null) {
                        existingSharedDinner.setHomePage(sharedDinner.getHomePage());
                    }
                    if (sharedDinner.getLatitude() != null) {
                        existingSharedDinner.setLatitude(sharedDinner.getLatitude());
                    }
                    if (sharedDinner.getLongitude() != null) {
                        existingSharedDinner.setLongitude(sharedDinner.getLongitude());
                    }
                    if (sharedDinner.getAddress() != null) {
                        existingSharedDinner.setAddress(sharedDinner.getAddress());
                    }
                    if (sharedDinner.getCostmin() != null) {
                        existingSharedDinner.setCostmin(sharedDinner.getCostmin());
                    }
                    if (sharedDinner.getCostmax() != null) {
                        existingSharedDinner.setCostmax(sharedDinner.getCostmax());
                    }

                    return existingSharedDinner;
                }
            )
            .map(sharedDinnerRepository::save)
            .map(
                savedSharedDinner -> {
                    sharedDinnerSearchRepository.save(savedSharedDinner);

                    return savedSharedDinner;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SharedDinner> findAll(Pageable pageable) {
        log.debug("Request to get all SharedDinners");
        return sharedDinnerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SharedDinner> findOne(Long id) {
        log.debug("Request to get SharedDinner : {}", id);
        return sharedDinnerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SharedDinner : {}", id);
        sharedDinnerRepository.deleteById(id);
        sharedDinnerSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SharedDinner> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SharedDinners for query {}", query);
        return sharedDinnerSearchRepository.search(queryStringQuery(query), pageable);
    }
}
