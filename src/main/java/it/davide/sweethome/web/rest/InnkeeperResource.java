package it.davide.sweethome.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import it.davide.sweethome.domain.Innkeeper;
import it.davide.sweethome.repository.InnkeeperRepository;
import it.davide.sweethome.service.InnkeeperService;
import it.davide.sweethome.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.davide.sweethome.domain.Innkeeper}.
 */
@RestController
@RequestMapping("/api")
public class InnkeeperResource {

    private final Logger log = LoggerFactory.getLogger(InnkeeperResource.class);

    private static final String ENTITY_NAME = "innkeeper";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InnkeeperService innkeeperService;

    private final InnkeeperRepository innkeeperRepository;

    public InnkeeperResource(InnkeeperService innkeeperService, InnkeeperRepository innkeeperRepository) {
        this.innkeeperService = innkeeperService;
        this.innkeeperRepository = innkeeperRepository;
    }

    /**
     * {@code POST  /innkeepers} : Create a new innkeeper.
     *
     * @param innkeeper the innkeeper to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new innkeeper, or with status {@code 400 (Bad Request)} if the innkeeper has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/innkeepers")
    public ResponseEntity<Innkeeper> createInnkeeper(@Valid @RequestBody Innkeeper innkeeper) throws URISyntaxException {
        log.debug("REST request to save Innkeeper : {}", innkeeper);
        if (innkeeper.getId() != null) {
            throw new BadRequestAlertException("A new innkeeper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Innkeeper result = innkeeperService.save(innkeeper);
        return ResponseEntity
            .created(new URI("/api/innkeepers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /innkeepers/:id} : Updates an existing innkeeper.
     *
     * @param id the id of the innkeeper to save.
     * @param innkeeper the innkeeper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated innkeeper,
     * or with status {@code 400 (Bad Request)} if the innkeeper is not valid,
     * or with status {@code 500 (Internal Server Error)} if the innkeeper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/innkeepers/{id}")
    public ResponseEntity<Innkeeper> updateInnkeeper(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Innkeeper innkeeper
    ) throws URISyntaxException {
        log.debug("REST request to update Innkeeper : {}, {}", id, innkeeper);
        if (innkeeper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, innkeeper.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!innkeeperRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Innkeeper result = innkeeperService.save(innkeeper);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, innkeeper.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /innkeepers/:id} : Partial updates given fields of an existing innkeeper, field will ignore if it is null
     *
     * @param id the id of the innkeeper to save.
     * @param innkeeper the innkeeper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated innkeeper,
     * or with status {@code 400 (Bad Request)} if the innkeeper is not valid,
     * or with status {@code 404 (Not Found)} if the innkeeper is not found,
     * or with status {@code 500 (Internal Server Error)} if the innkeeper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/innkeepers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Innkeeper> partialUpdateInnkeeper(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Innkeeper innkeeper
    ) throws URISyntaxException {
        log.debug("REST request to partial update Innkeeper partially : {}, {}", id, innkeeper);
        if (innkeeper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, innkeeper.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!innkeeperRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Innkeeper> result = innkeeperService.partialUpdate(innkeeper);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, innkeeper.getId().toString())
        );
    }

    /**
     * {@code GET  /innkeepers} : get all the innkeepers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of innkeepers in body.
     */
    @GetMapping("/innkeepers")
    public ResponseEntity<List<Innkeeper>> getAllInnkeepers(Pageable pageable) {
        log.debug("REST request to get a page of Innkeepers");
        Page<Innkeeper> page = innkeeperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /innkeepers/:id} : get the "id" innkeeper.
     *
     * @param id the id of the innkeeper to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the innkeeper, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/innkeepers/{id}")
    public ResponseEntity<Innkeeper> getInnkeeper(@PathVariable Long id) {
        log.debug("REST request to get Innkeeper : {}", id);
        Optional<Innkeeper> innkeeper = innkeeperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(innkeeper);
    }

    /**
     * {@code DELETE  /innkeepers/:id} : delete the "id" innkeeper.
     *
     * @param id the id of the innkeeper to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/innkeepers/{id}")
    public ResponseEntity<Void> deleteInnkeeper(@PathVariable Long id) {
        log.debug("REST request to delete Innkeeper : {}", id);
        innkeeperService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/innkeepers?query=:query} : search for the innkeeper corresponding
     * to the query.
     *
     * @param query the query of the innkeeper search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/innkeepers")
    public ResponseEntity<List<Innkeeper>> searchInnkeepers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Innkeepers for query {}", query);
        Page<Innkeeper> page = innkeeperService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
