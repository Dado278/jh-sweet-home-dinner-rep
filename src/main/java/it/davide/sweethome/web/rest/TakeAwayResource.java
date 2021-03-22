package it.davide.sweethome.web.rest;

import it.davide.sweethome.domain.TakeAway;
import it.davide.sweethome.service.TakeAwayService;
import it.davide.sweethome.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link it.davide.sweethome.domain.TakeAway}.
 */
@RestController
@RequestMapping("/api")
public class TakeAwayResource {

    private final Logger log = LoggerFactory.getLogger(TakeAwayResource.class);

    private static final String ENTITY_NAME = "takeAway";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TakeAwayService takeAwayService;

    public TakeAwayResource(TakeAwayService takeAwayService) {
        this.takeAwayService = takeAwayService;
    }

    /**
     * {@code POST  /take-aways} : Create a new takeAway.
     *
     * @param takeAway the takeAway to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new takeAway, or with status {@code 400 (Bad Request)} if the takeAway has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/take-aways")
    public ResponseEntity<TakeAway> createTakeAway(@Valid @RequestBody TakeAway takeAway) throws URISyntaxException {
        log.debug("REST request to save TakeAway : {}", takeAway);
        if (takeAway.getId() != null) {
            throw new BadRequestAlertException("A new takeAway cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TakeAway result = takeAwayService.save(takeAway);
        return ResponseEntity.created(new URI("/api/take-aways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /take-aways} : Updates an existing takeAway.
     *
     * @param takeAway the takeAway to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated takeAway,
     * or with status {@code 400 (Bad Request)} if the takeAway is not valid,
     * or with status {@code 500 (Internal Server Error)} if the takeAway couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/take-aways")
    public ResponseEntity<TakeAway> updateTakeAway(@Valid @RequestBody TakeAway takeAway) throws URISyntaxException {
        log.debug("REST request to update TakeAway : {}", takeAway);
        if (takeAway.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TakeAway result = takeAwayService.save(takeAway);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, takeAway.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /take-aways} : get all the takeAways.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of takeAways in body.
     */
    @GetMapping("/take-aways")
    public ResponseEntity<List<TakeAway>> getAllTakeAways(Pageable pageable) {
        log.debug("REST request to get a page of TakeAways");
        Page<TakeAway> page = takeAwayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /take-aways/:id} : get the "id" takeAway.
     *
     * @param id the id of the takeAway to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the takeAway, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/take-aways/{id}")
    public ResponseEntity<TakeAway> getTakeAway(@PathVariable Long id) {
        log.debug("REST request to get TakeAway : {}", id);
        Optional<TakeAway> takeAway = takeAwayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(takeAway);
    }

    /**
     * {@code DELETE  /take-aways/:id} : delete the "id" takeAway.
     *
     * @param id the id of the takeAway to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/take-aways/{id}")
    public ResponseEntity<Void> deleteTakeAway(@PathVariable Long id) {
        log.debug("REST request to delete TakeAway : {}", id);
        takeAwayService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/take-aways?query=:query} : search for the takeAway corresponding
     * to the query.
     *
     * @param query the query of the takeAway search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/take-aways")
    public ResponseEntity<List<TakeAway>> searchTakeAways(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TakeAways for query {}", query);
        Page<TakeAway> page = takeAwayService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
