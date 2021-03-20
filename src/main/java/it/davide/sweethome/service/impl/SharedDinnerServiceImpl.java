package it.davide.sweethome.service.impl;

import it.davide.sweethome.service.SharedDinnerService;
import it.davide.sweethome.domain.SharedDinner;
import it.davide.sweethome.repository.SharedDinnerRepository;
import it.davide.sweethome.repository.search.SharedDinnerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link SharedDinner}.
 */
@Service
@Transactional
public class SharedDinnerServiceImpl implements SharedDinnerService {

    private final Logger log = LoggerFactory.getLogger(SharedDinnerServiceImpl.class);

    private final SharedDinnerRepository sharedDinnerRepository;

    private final SharedDinnerSearchRepository sharedDinnerSearchRepository;

    public SharedDinnerServiceImpl(SharedDinnerRepository sharedDinnerRepository, SharedDinnerSearchRepository sharedDinnerSearchRepository) {
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
        return sharedDinnerSearchRepository.search(queryStringQuery(query), pageable);    }
}
