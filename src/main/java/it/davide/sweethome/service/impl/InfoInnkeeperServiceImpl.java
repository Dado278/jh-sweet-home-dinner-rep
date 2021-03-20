package it.davide.sweethome.service.impl;

import it.davide.sweethome.service.InfoInnkeeperService;
import it.davide.sweethome.domain.InfoInnkeeper;
import it.davide.sweethome.repository.InfoInnkeeperRepository;
import it.davide.sweethome.repository.search.InfoInnkeeperSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link InfoInnkeeper}.
 */
@Service
@Transactional
public class InfoInnkeeperServiceImpl implements InfoInnkeeperService {

    private final Logger log = LoggerFactory.getLogger(InfoInnkeeperServiceImpl.class);

    private final InfoInnkeeperRepository infoInnkeeperRepository;

    private final InfoInnkeeperSearchRepository infoInnkeeperSearchRepository;

    public InfoInnkeeperServiceImpl(InfoInnkeeperRepository infoInnkeeperRepository, InfoInnkeeperSearchRepository infoInnkeeperSearchRepository) {
        this.infoInnkeeperRepository = infoInnkeeperRepository;
        this.infoInnkeeperSearchRepository = infoInnkeeperSearchRepository;
    }

    @Override
    public InfoInnkeeper save(InfoInnkeeper infoInnkeeper) {
        log.debug("Request to save InfoInnkeeper : {}", infoInnkeeper);
        InfoInnkeeper result = infoInnkeeperRepository.save(infoInnkeeper);
        infoInnkeeperSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InfoInnkeeper> findAll(Pageable pageable) {
        log.debug("Request to get all InfoInnkeepers");
        return infoInnkeeperRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InfoInnkeeper> findOne(Long id) {
        log.debug("Request to get InfoInnkeeper : {}", id);
        return infoInnkeeperRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InfoInnkeeper : {}", id);
        infoInnkeeperRepository.deleteById(id);
        infoInnkeeperSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InfoInnkeeper> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InfoInnkeepers for query {}", query);
        return infoInnkeeperSearchRepository.search(queryStringQuery(query), pageable);    }
}
