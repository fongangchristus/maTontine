package com.it4innov.web.rest;

import com.it4innov.repository.CotisationTontineRepository;
import com.it4innov.service.CotisationTontineQueryService;
import com.it4innov.service.CotisationTontineService;
import com.it4innov.service.criteria.CotisationTontineCriteria;
import com.it4innov.service.dto.CotisationTontineDTO;
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
 * REST controller for managing {@link com.it4innov.domain.CotisationTontine}.
 */
@RestController
@RequestMapping("/api")
public class CotisationTontineResource {

    private final Logger log = LoggerFactory.getLogger(CotisationTontineResource.class);

    private static final String ENTITY_NAME = "cotisationTontine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CotisationTontineService cotisationTontineService;

    private final CotisationTontineRepository cotisationTontineRepository;

    private final CotisationTontineQueryService cotisationTontineQueryService;

    public CotisationTontineResource(
        CotisationTontineService cotisationTontineService,
        CotisationTontineRepository cotisationTontineRepository,
        CotisationTontineQueryService cotisationTontineQueryService
    ) {
        this.cotisationTontineService = cotisationTontineService;
        this.cotisationTontineRepository = cotisationTontineRepository;
        this.cotisationTontineQueryService = cotisationTontineQueryService;
    }

    /**
     * {@code POST  /cotisation-tontines} : Create a new cotisationTontine.
     *
     * @param cotisationTontineDTO the cotisationTontineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cotisationTontineDTO, or with status {@code 400 (Bad Request)} if the cotisationTontine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cotisation-tontines")
    public ResponseEntity<CotisationTontineDTO> createCotisationTontine(@RequestBody CotisationTontineDTO cotisationTontineDTO)
        throws URISyntaxException {
        log.debug("REST request to save CotisationTontine : {}", cotisationTontineDTO);
        if (cotisationTontineDTO.getId() != null) {
            throw new BadRequestAlertException("A new cotisationTontine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CotisationTontineDTO result = cotisationTontineService.save(cotisationTontineDTO);
        return ResponseEntity
            .created(new URI("/api/cotisation-tontines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cotisation-tontines/:id} : Updates an existing cotisationTontine.
     *
     * @param id the id of the cotisationTontineDTO to save.
     * @param cotisationTontineDTO the cotisationTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cotisationTontineDTO,
     * or with status {@code 400 (Bad Request)} if the cotisationTontineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cotisationTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cotisation-tontines/{id}")
    public ResponseEntity<CotisationTontineDTO> updateCotisationTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CotisationTontineDTO cotisationTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CotisationTontine : {}, {}", id, cotisationTontineDTO);
        if (cotisationTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cotisationTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cotisationTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CotisationTontineDTO result = cotisationTontineService.update(cotisationTontineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cotisationTontineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cotisation-tontines/:id} : Partial updates given fields of an existing cotisationTontine, field will ignore if it is null
     *
     * @param id the id of the cotisationTontineDTO to save.
     * @param cotisationTontineDTO the cotisationTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cotisationTontineDTO,
     * or with status {@code 400 (Bad Request)} if the cotisationTontineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cotisationTontineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cotisationTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cotisation-tontines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CotisationTontineDTO> partialUpdateCotisationTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CotisationTontineDTO cotisationTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CotisationTontine partially : {}, {}", id, cotisationTontineDTO);
        if (cotisationTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cotisationTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cotisationTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CotisationTontineDTO> result = cotisationTontineService.partialUpdate(cotisationTontineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cotisationTontineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cotisation-tontines} : get all the cotisationTontines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cotisationTontines in body.
     */
    @GetMapping("/cotisation-tontines")
    public ResponseEntity<List<CotisationTontineDTO>> getAllCotisationTontines(
        CotisationTontineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CotisationTontines by criteria: {}", criteria);
        Page<CotisationTontineDTO> page = cotisationTontineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cotisation-tontines/count} : count all the cotisationTontines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cotisation-tontines/count")
    public ResponseEntity<Long> countCotisationTontines(CotisationTontineCriteria criteria) {
        log.debug("REST request to count CotisationTontines by criteria: {}", criteria);
        return ResponseEntity.ok().body(cotisationTontineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cotisation-tontines/:id} : get the "id" cotisationTontine.
     *
     * @param id the id of the cotisationTontineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cotisationTontineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cotisation-tontines/{id}")
    public ResponseEntity<CotisationTontineDTO> getCotisationTontine(@PathVariable Long id) {
        log.debug("REST request to get CotisationTontine : {}", id);
        Optional<CotisationTontineDTO> cotisationTontineDTO = cotisationTontineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cotisationTontineDTO);
    }

    /**
     * {@code DELETE  /cotisation-tontines/:id} : delete the "id" cotisationTontine.
     *
     * @param id the id of the cotisationTontineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cotisation-tontines/{id}")
    public ResponseEntity<Void> deleteCotisationTontine(@PathVariable Long id) {
        log.debug("REST request to delete CotisationTontine : {}", id);
        cotisationTontineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
