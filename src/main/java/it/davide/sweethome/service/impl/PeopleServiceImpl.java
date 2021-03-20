package it.davide.sweethome.service.impl;

import it.davide.sweethome.service.PeopleService;
import it.davide.sweethome.domain.People;
import it.davide.sweethome.repository.PeopleRepository;
import it.davide.sweethome.repository.search.PeopleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link People}.
 */
@Service
@Transactional
public class PeopleServiceImpl implements PeopleService {

    private final Logger log = LoggerFactory.getLogger(PeopleServiceImpl.class);

    private final PeopleRepository peopleRepository;

    private final PeopleSearchRepository peopleSearchRepository;

    public PeopleServiceImpl(PeopleRepository peopleRepository, PeopleSearchRepository peopleSearchRepository) {
        this.peopleRepository = peopleRepository;
        this.peopleSearchRepository = peopleSearchRepository;
    }

    @Override
    public People save(People people) {
        log.debug("Request to save People : {}", people);
        People result = peopleRepository.save(people);
        peopleSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<People> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return peopleRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<People> findOne(Long id) {
        log.debug("Request to get People : {}", id);
        return peopleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete People : {}", id);
        peopleRepository.deleteById(id);
        peopleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<People> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of People for query {}", query);
        return peopleSearchRepository.search(queryStringQuery(query), pageable);    }
}
