package it.davide.sweethome.service.impl;

import it.davide.sweethome.service.InnkeeperService;
import it.davide.sweethome.domain.Innkeeper;
import it.davide.sweethome.repository.InnkeeperRepository;
import it.davide.sweethome.repository.search.InnkeeperSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Innkeeper}.
 */
@Service
@Transactional
public class InnkeeperServiceImpl implements InnkeeperService {

    private final Logger log = LoggerFactory.getLogger(InnkeeperServiceImpl.class);

    private final InnkeeperRepository innkeeperRepository;

    private final InnkeeperSearchRepository innkeeperSearchRepository;

    public InnkeeperServiceImpl(InnkeeperRepository innkeeperRepository, InnkeeperSearchRepository innkeeperSearchRepository) {
        this.innkeeperRepository = innkeeperRepository;
        this.innkeeperSearchRepository = innkeeperSearchRepository;
    }

    @Override
    public Innkeeper save(Innkeeper innkeeper) {
        log.debug("Request to save Innkeeper : {}", innkeeper);
        Innkeeper result = innkeeperRepository.save(innkeeper);
        innkeeperSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Innkeeper> findAll(Pageable pageable) {
        log.debug("Request to get all Innkeepers");
        return innkeeperRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Innkeeper> findOne(Long id) {
        log.debug("Request to get Innkeeper : {}", id);
        return innkeeperRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Innkeeper : {}", id);
        innkeeperRepository.deleteById(id);
        innkeeperSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Innkeeper> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Innkeepers for query {}", query);
        return innkeeperSearchRepository.search(queryStringQuery(query), pageable);    }
}
