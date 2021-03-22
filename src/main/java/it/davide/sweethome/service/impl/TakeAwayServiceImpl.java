package it.davide.sweethome.service.impl;

import it.davide.sweethome.service.TakeAwayService;
import it.davide.sweethome.domain.TakeAway;
import it.davide.sweethome.repository.TakeAwayRepository;
import it.davide.sweethome.repository.search.TakeAwaySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TakeAway}.
 */
@Service
@Transactional
public class TakeAwayServiceImpl implements TakeAwayService {

    private final Logger log = LoggerFactory.getLogger(TakeAwayServiceImpl.class);

    private final TakeAwayRepository takeAwayRepository;

    private final TakeAwaySearchRepository takeAwaySearchRepository;

    public TakeAwayServiceImpl(TakeAwayRepository takeAwayRepository, TakeAwaySearchRepository takeAwaySearchRepository) {
        this.takeAwayRepository = takeAwayRepository;
        this.takeAwaySearchRepository = takeAwaySearchRepository;
    }

    @Override
    public TakeAway save(TakeAway takeAway) {
        log.debug("Request to save TakeAway : {}", takeAway);
        TakeAway result = takeAwayRepository.save(takeAway);
        takeAwaySearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TakeAway> findAll(Pageable pageable) {
        log.debug("Request to get all TakeAways");
        return takeAwayRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TakeAway> findOne(Long id) {
        log.debug("Request to get TakeAway : {}", id);
        return takeAwayRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TakeAway : {}", id);
        takeAwayRepository.deleteById(id);
        takeAwaySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TakeAway> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TakeAways for query {}", query);
        return takeAwaySearchRepository.search(queryStringQuery(query), pageable);    }
}
