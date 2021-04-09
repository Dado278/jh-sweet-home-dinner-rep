package it.davide.sweethome.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import it.davide.sweethome.domain.Innkeeper;
import it.davide.sweethome.repository.InnkeeperRepository;
import it.davide.sweethome.repository.search.InnkeeperSearchRepository;
import it.davide.sweethome.service.InnkeeperService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Optional<Innkeeper> partialUpdate(Innkeeper innkeeper) {
        log.debug("Request to partially update Innkeeper : {}", innkeeper);

        return innkeeperRepository
            .findById(innkeeper.getId())
            .map(
                existingInnkeeper -> {
                    if (innkeeper.getNickname() != null) {
                        existingInnkeeper.setNickname(innkeeper.getNickname());
                    }
                    if (innkeeper.getFreshman() != null) {
                        existingInnkeeper.setFreshman(innkeeper.getFreshman());
                    }
                    if (innkeeper.getEmail() != null) {
                        existingInnkeeper.setEmail(innkeeper.getEmail());
                    }
                    if (innkeeper.getPhoneNumber() != null) {
                        existingInnkeeper.setPhoneNumber(innkeeper.getPhoneNumber());
                    }
                    if (innkeeper.getGender() != null) {
                        existingInnkeeper.setGender(innkeeper.getGender());
                    }
                    if (innkeeper.getSlogan() != null) {
                        existingInnkeeper.setSlogan(innkeeper.getSlogan());
                    }
                    if (innkeeper.getDescription() != null) {
                        existingInnkeeper.setDescription(innkeeper.getDescription());
                    }
                    if (innkeeper.getHomePage() != null) {
                        existingInnkeeper.setHomePage(innkeeper.getHomePage());
                    }
                    if (innkeeper.getLatitude() != null) {
                        existingInnkeeper.setLatitude(innkeeper.getLatitude());
                    }
                    if (innkeeper.getLongitude() != null) {
                        existingInnkeeper.setLongitude(innkeeper.getLongitude());
                    }
                    if (innkeeper.getAddress() != null) {
                        existingInnkeeper.setAddress(innkeeper.getAddress());
                    }
                    if (innkeeper.getServices() != null) {
                        existingInnkeeper.setServices(innkeeper.getServices());
                    }
                    if (innkeeper.getCreateDate() != null) {
                        existingInnkeeper.setCreateDate(innkeeper.getCreateDate());
                    }
                    if (innkeeper.getUpdateDate() != null) {
                        existingInnkeeper.setUpdateDate(innkeeper.getUpdateDate());
                    }

                    return existingInnkeeper;
                }
            )
            .map(innkeeperRepository::save)
            .map(
                savedInnkeeper -> {
                    innkeeperSearchRepository.save(savedInnkeeper);

                    return savedInnkeeper;
                }
            );
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
        return innkeeperSearchRepository.search(queryStringQuery(query), pageable);
    }
}
