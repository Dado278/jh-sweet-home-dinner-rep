package it.davide.sweethome.web.rest;

import it.davide.sweethome.domain.InfoInnkeeper;
import it.davide.sweethome.service.InfoInnkeeperService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link it.davide.sweethome.domain.InfoInnkeeper}.
 */
@RestController
@RequestMapping("/api")
public class InfoInnkeeperResource {

    private final Logger log = LoggerFactory.getLogger(InfoInnkeeperResource.class);

    private static final String ENTITY_NAME = "infoInnkeeper";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfoInnkeeperService infoInnkeeperService;

    public InfoInnkeeperResource(InfoInnkeeperService infoInnkeeperService) {
        this.infoInnkeeperService = infoInnkeeperService;
    }

    /**
     * {@code POST  /info-innkeepers} : Create a new infoInnkeeper.
     *
     * @param infoInnkeeper the infoInnkeeper to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infoInnkeeper, or with status {@code 400 (Bad Request)} if the infoInnkeeper has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/info-innkeepers")
    public ResponseEntity<InfoInnkeeper> createInfoInnkeeper(@RequestBody InfoInnkeeper infoInnkeeper) throws URISyntaxException {
        log.debug("REST request to save InfoInnkeeper : {}", infoInnkeeper);
        if (infoInnkeeper.getId() != null) {
            throw new BadRequestAlertException("A new infoInnkeeper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfoInnkeeper result = infoInnkeeperService.save(infoInnkeeper);
        return ResponseEntity.created(new URI("/api/info-innkeepers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /info-innkeepers} : Updates an existing infoInnkeeper.
     *
     * @param infoInnkeeper the infoInnkeeper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoInnkeeper,
     * or with status {@code 400 (Bad Request)} if the infoInnkeeper is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infoInnkeeper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/info-innkeepers")
    public ResponseEntity<InfoInnkeeper> updateInfoInnkeeper(@RequestBody InfoInnkeeper infoInnkeeper) throws URISyntaxException {
        log.debug("REST request to update InfoInnkeeper : {}", infoInnkeeper);
        if (infoInnkeeper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InfoInnkeeper result = infoInnkeeperService.save(infoInnkeeper);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infoInnkeeper.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /info-innkeepers} : get all the infoInnkeepers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infoInnkeepers in body.
     */
    @GetMapping("/info-innkeepers")
    public ResponseEntity<List<InfoInnkeeper>> getAllInfoInnkeepers(Pageable pageable) {
        log.debug("REST request to get a page of InfoInnkeepers");
        Page<InfoInnkeeper> page = infoInnkeeperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /info-innkeepers/:id} : get the "id" infoInnkeeper.
     *
     * @param id the id of the infoInnkeeper to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infoInnkeeper, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/info-innkeepers/{id}")
    public ResponseEntity<InfoInnkeeper> getInfoInnkeeper(@PathVariable Long id) {
        log.debug("REST request to get InfoInnkeeper : {}", id);
        Optional<InfoInnkeeper> infoInnkeeper = infoInnkeeperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infoInnkeeper);
    }

    /**
     * {@code DELETE  /info-innkeepers/:id} : delete the "id" infoInnkeeper.
     *
     * @param id the id of the infoInnkeeper to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/info-innkeepers/{id}")
    public ResponseEntity<Void> deleteInfoInnkeeper(@PathVariable Long id) {
        log.debug("REST request to delete InfoInnkeeper : {}", id);
        infoInnkeeperService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/info-innkeepers?query=:query} : search for the infoInnkeeper corresponding
     * to the query.
     *
     * @param query the query of the infoInnkeeper search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/info-innkeepers")
    public ResponseEntity<List<InfoInnkeeper>> searchInfoInnkeepers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InfoInnkeepers for query {}", query);
        Page<InfoInnkeeper> page = infoInnkeeperService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
