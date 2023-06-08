package com.it4innov.web.rest;

import com.it4innov.repository.DecaissementTontineRepository;
import com.it4innov.service.DecaissementTontineQueryService;
import com.it4innov.service.DecaissementTontineService;
import com.it4innov.service.criteria.DecaissementTontineCriteria;
import com.it4innov.service.dto.DecaissementTontineDTO;
import com.it4innov.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.it4innov.domain.DecaissementTontine}.
 */
@RestController
@RequestMapping("/api")
public class DecaissementTontineResource {

    private final Logger log = LoggerFactory.getLogger(DecaissementTontineResource.class);

    private static final String ENTITY_NAME = "decaissementTontine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecaissementTontineService decaissementTontineService;

    private final DecaissementTontineRepository decaissementTontineRepository;

    private final DecaissementTontineQueryService decaissementTontineQueryService;

    public DecaissementTontineResource(
        DecaissementTontineService decaissementTontineService,
        DecaissementTontineRepository decaissementTontineRepository,
        DecaissementTontineQueryService decaissementTontineQueryService
    ) {
        this.decaissementTontineService = decaissementTontineService;
        this.decaissementTontineRepository = decaissementTontineRepository;
        this.decaissementTontineQueryService = decaissementTontineQueryService;
    }

    /**
     * {@code POST  /decaissement-tontines} : Create a new decaissementTontine.
     *
     * @param decaissementTontineDTO the decaissementTontineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decaissementTontineDTO, or with status {@code 400 (Bad Request)} if the decaissementTontine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/decaissement-tontines")
    public ResponseEntity<DecaissementTontineDTO> createDecaissementTontine(@RequestBody DecaissementTontineDTO decaissementTontineDTO)
        throws URISyntaxException {
        log.debug("REST request to save DecaissementTontine : {}", decaissementTontineDTO);
        if (decaissementTontineDTO.getId() != null) {
            throw new BadRequestAlertException("A new decaissementTontine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DecaissementTontineDTO result = decaissementTontineService.save(decaissementTontineDTO);
        return ResponseEntity
            .created(new URI("/api/decaissement-tontines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /decaissement-tontines/:id} : Updates an existing decaissementTontine.
     *
     * @param id the id of the decaissementTontineDTO to save.
     * @param decaissementTontineDTO the decaissementTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decaissementTontineDTO,
     * or with status {@code 400 (Bad Request)} if the decaissementTontineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decaissementTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/decaissement-tontines/{id}")
    public ResponseEntity<DecaissementTontineDTO> updateDecaissementTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DecaissementTontineDTO decaissementTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DecaissementTontine : {}, {}", id, decaissementTontineDTO);
        if (decaissementTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decaissementTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decaissementTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DecaissementTontineDTO result = decaissementTontineService.update(decaissementTontineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decaissementTontineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /decaissement-tontines/:id} : Partial updates given fields of an existing decaissementTontine, field will ignore if it is null
     *
     * @param id the id of the decaissementTontineDTO to save.
     * @param decaissementTontineDTO the decaissementTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decaissementTontineDTO,
     * or with status {@code 400 (Bad Request)} if the decaissementTontineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the decaissementTontineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the decaissementTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/decaissement-tontines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DecaissementTontineDTO> partialUpdateDecaissementTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DecaissementTontineDTO decaissementTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DecaissementTontine partially : {}, {}", id, decaissementTontineDTO);
        if (decaissementTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decaissementTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decaissementTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DecaissementTontineDTO> result = decaissementTontineService.partialUpdate(decaissementTontineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decaissementTontineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /decaissement-tontines} : get all the decaissementTontines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decaissementTontines in body.
     */
    @GetMapping("/decaissement-tontines")
    public ResponseEntity<List<DecaissementTontineDTO>> getAllDecaissementTontines(
        DecaissementTontineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DecaissementTontines by criteria: {}", criteria);
        Page<DecaissementTontineDTO> page = decaissementTontineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /decaissement-tontines/count} : count all the decaissementTontines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/decaissement-tontines/count")
    public ResponseEntity<Long> countDecaissementTontines(DecaissementTontineCriteria criteria) {
        log.debug("REST request to count DecaissementTontines by criteria: {}", criteria);
        return ResponseEntity.ok().body(decaissementTontineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /decaissement-tontines/:id} : get the "id" decaissementTontine.
     *
     * @param id the id of the decaissementTontineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decaissementTontineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/decaissement-tontines/{id}")
    public ResponseEntity<DecaissementTontineDTO> getDecaissementTontine(@PathVariable Long id) {
        log.debug("REST request to get DecaissementTontine : {}", id);
        Optional<DecaissementTontineDTO> decaissementTontineDTO = decaissementTontineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decaissementTontineDTO);
    }

    /**
     * {@code DELETE  /decaissement-tontines/:id} : delete the "id" decaissementTontine.
     *
     * @param id the id of the decaissementTontineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/decaissement-tontines/{id}")
    public ResponseEntity<Void> deleteDecaissementTontine(@PathVariable Long id) {
        log.debug("REST request to delete DecaissementTontine : {}", id);
        decaissementTontineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
