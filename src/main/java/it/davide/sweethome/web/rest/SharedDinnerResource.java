package it.davide.sweethome.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import it.davide.sweethome.domain.SharedDinner;
import it.davide.sweethome.repository.SharedDinnerRepository;
import it.davide.sweethome.service.SharedDinnerService;
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
//import tech.jhipster.web.util.HeaderUtil;
//import tech.jhipster.web.util.PaginationUtil;
//import tech.jhipster.web.util.ResponseUtil;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.davide.sweethome.domain.SharedDinner}.
 */
@RestController
@RequestMapping("/api")
public class SharedDinnerResource {

    private final Logger log = LoggerFactory.getLogger(SharedDinnerResource.class);

    private static final String ENTITY_NAME = "sharedDinner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SharedDinnerService sharedDinnerService;

    private final SharedDinnerRepository sharedDinnerRepository;

    public SharedDinnerResource(SharedDinnerService sharedDinnerService, SharedDinnerRepository sharedDinnerRepository) {
        this.sharedDinnerService = sharedDinnerService;
        this.sharedDinnerRepository = sharedDinnerRepository;
    }

    /**
     * {@code POST  /shared-dinners} : Create a new sharedDinner.
     *
     * @param sharedDinner the sharedDinner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sharedDinner, or with status {@code 400 (Bad Request)} if the sharedDinner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shared-dinners")
    public ResponseEntity<SharedDinner> createSharedDinner(@Valid @RequestBody SharedDinner sharedDinner) throws URISyntaxException {
        log.debug("REST request to save SharedDinner : {}", sharedDinner);
        if (sharedDinner.getId() != null) {
            throw new BadRequestAlertException("A new sharedDinner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SharedDinner result = sharedDinnerService.save(sharedDinner);
        return ResponseEntity
            .created(new URI("/api/shared-dinners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shared-dinners/:id} : Updates an existing sharedDinner.
     *
     * @param id the id of the sharedDinner to save.
     * @param sharedDinner the sharedDinner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sharedDinner,
     * or with status {@code 400 (Bad Request)} if the sharedDinner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sharedDinner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shared-dinners/{id}")
    public ResponseEntity<SharedDinner> updateSharedDinner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SharedDinner sharedDinner
    ) throws URISyntaxException {
        log.debug("REST request to update SharedDinner : {}, {}", id, sharedDinner);
        if (sharedDinner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sharedDinner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sharedDinnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SharedDinner result = sharedDinnerService.save(sharedDinner);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sharedDinner.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shared-dinners/:id} : Partial updates given fields of an existing sharedDinner, field will ignore if it is null
     *
     * @param id the id of the sharedDinner to save.
     * @param sharedDinner the sharedDinner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sharedDinner,
     * or with status {@code 400 (Bad Request)} if the sharedDinner is not valid,
     * or with status {@code 404 (Not Found)} if the sharedDinner is not found,
     * or with status {@code 500 (Internal Server Error)} if the sharedDinner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shared-dinners/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SharedDinner> partialUpdateSharedDinner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SharedDinner sharedDinner
    ) throws URISyntaxException {
        log.debug("REST request to partial update SharedDinner partially : {}, {}", id, sharedDinner);
        if (sharedDinner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sharedDinner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sharedDinnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SharedDinner> result = sharedDinnerService.partialUpdate(sharedDinner);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sharedDinner.getId().toString())
        );
    }

    /**
     * {@code GET  /shared-dinners} : get all the sharedDinners.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sharedDinners in body.
     */
    @GetMapping("/shared-dinners")
    public ResponseEntity<List<SharedDinner>> getAllSharedDinners(Pageable pageable) {
        log.debug("REST request to get a page of SharedDinners");
        Page<SharedDinner> page = sharedDinnerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shared-dinners/:id} : get the "id" sharedDinner.
     *
     * @param id the id of the sharedDinner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sharedDinner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shared-dinners/{id}")
    public ResponseEntity<SharedDinner> getSharedDinner(@PathVariable Long id) {
        log.debug("REST request to get SharedDinner : {}", id);
        Optional<SharedDinner> sharedDinner = sharedDinnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sharedDinner);
    }

    /**
     * {@code DELETE  /shared-dinners/:id} : delete the "id" sharedDinner.
     *
     * @param id the id of the sharedDinner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shared-dinners/{id}")
    public ResponseEntity<Void> deleteSharedDinner(@PathVariable Long id) {
        log.debug("REST request to delete SharedDinner : {}", id);
        sharedDinnerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/shared-dinners?query=:query} : search for the sharedDinner corresponding
     * to the query.
     *
     * @param query the query of the sharedDinner search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/shared-dinners")
    public ResponseEntity<List<SharedDinner>> searchSharedDinners(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SharedDinners for query {}", query);
        Page<SharedDinner> page = sharedDinnerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
