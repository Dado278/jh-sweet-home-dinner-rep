package it.davide.sweethome.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import it.davide.sweethome.domain.TakeAway;
import it.davide.sweethome.repository.TakeAwayRepository;
import it.davide.sweethome.repository.search.TakeAwaySearchRepository;
import it.davide.sweethome.service.TakeAwayService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Optional<TakeAway> partialUpdate(TakeAway takeAway) {
        log.debug("Request to partially update TakeAway : {}", takeAway);

        return takeAwayRepository
            .findById(takeAway.getId())
            .map(
                existingTakeAway -> {
                    if (takeAway.getCreateDate() != null) {
                        existingTakeAway.setCreateDate(takeAway.getCreateDate());
                    }
                    if (takeAway.getUpdateDate() != null) {
                        existingTakeAway.setUpdateDate(takeAway.getUpdateDate());
                    }
                    if (takeAway.getDish() != null) {
                        existingTakeAway.setDish(takeAway.getDish());
                    }
                    if (takeAway.getDescription() != null) {
                        existingTakeAway.setDescription(takeAway.getDescription());
                    }
                    if (takeAway.getIngredients() != null) {
                        existingTakeAway.setIngredients(takeAway.getIngredients());
                    }
                    if (takeAway.getAllergens() != null) {
                        existingTakeAway.setAllergens(takeAway.getAllergens());
                    }
                    if (takeAway.getLatitude() != null) {
                        existingTakeAway.setLatitude(takeAway.getLatitude());
                    }
                    if (takeAway.getLongitude() != null) {
                        existingTakeAway.setLongitude(takeAway.getLongitude());
                    }
                    if (takeAway.getAddress() != null) {
                        existingTakeAway.setAddress(takeAway.getAddress());
                    }
                    if (takeAway.getCostmin() != null) {
                        existingTakeAway.setCostmin(takeAway.getCostmin());
                    }
                    if (takeAway.getCostmax() != null) {
                        existingTakeAway.setCostmax(takeAway.getCostmax());
                    }
                    if (takeAway.getTags() != null) {
                        existingTakeAway.setTags(takeAway.getTags());
                    }

                    return existingTakeAway;
                }
            )
            .map(takeAwayRepository::save)
            .map(
                savedTakeAway -> {
                    takeAwaySearchRepository.save(savedTakeAway);

                    return savedTakeAway;
                }
            );
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
        return takeAwaySearchRepository.search(queryStringQuery(query), pageable);
    }
}
