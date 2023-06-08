package com.it4innov.web.rest;

import com.it4innov.repository.PaiementTontineRepository;
import com.it4innov.service.PaiementTontineQueryService;
import com.it4innov.service.PaiementTontineService;
import com.it4innov.service.criteria.PaiementTontineCriteria;
import com.it4innov.service.dto.PaiementTontineDTO;
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
 * REST controller for managing {@link com.it4innov.domain.PaiementTontine}.
 */
@RestController
@RequestMapping("/api")
public class PaiementTontineResource {

    private final Logger log = LoggerFactory.getLogger(PaiementTontineResource.class);

    private static final String ENTITY_NAME = "paiementTontine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaiementTontineService paiementTontineService;

    private final PaiementTontineRepository paiementTontineRepository;

    private final PaiementTontineQueryService paiementTontineQueryService;

    public PaiementTontineResource(
        PaiementTontineService paiementTontineService,
        PaiementTontineRepository paiementTontineRepository,
        PaiementTontineQueryService paiementTontineQueryService
    ) {
        this.paiementTontineService = paiementTontineService;
        this.paiementTontineRepository = paiementTontineRepository;
        this.paiementTontineQueryService = paiementTontineQueryService;
    }

    /**
     * {@code POST  /paiement-tontines} : Create a new paiementTontine.
     *
     * @param paiementTontineDTO the paiementTontineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiementTontineDTO, or with status {@code 400 (Bad Request)} if the paiementTontine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paiement-tontines")
    public ResponseEntity<PaiementTontineDTO> createPaiementTontine(@RequestBody PaiementTontineDTO paiementTontineDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaiementTontine : {}", paiementTontineDTO);
        if (paiementTontineDTO.getId() != null) {
            throw new BadRequestAlertException("A new paiementTontine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaiementTontineDTO result = paiementTontineService.save(paiementTontineDTO);
        return ResponseEntity
            .created(new URI("/api/paiement-tontines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiement-tontines/:id} : Updates an existing paiementTontine.
     *
     * @param id the id of the paiementTontineDTO to save.
     * @param paiementTontineDTO the paiementTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementTontineDTO,
     * or with status {@code 400 (Bad Request)} if the paiementTontineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiementTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paiement-tontines/{id}")
    public ResponseEntity<PaiementTontineDTO> updatePaiementTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaiementTontineDTO paiementTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaiementTontine : {}, {}", id, paiementTontineDTO);
        if (paiementTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaiementTontineDTO result = paiementTontineService.update(paiementTontineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementTontineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paiement-tontines/:id} : Partial updates given fields of an existing paiementTontine, field will ignore if it is null
     *
     * @param id the id of the paiementTontineDTO to save.
     * @param paiementTontineDTO the paiementTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementTontineDTO,
     * or with status {@code 400 (Bad Request)} if the paiementTontineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paiementTontineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paiementTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paiement-tontines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaiementTontineDTO> partialUpdatePaiementTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaiementTontineDTO paiementTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaiementTontine partially : {}, {}", id, paiementTontineDTO);
        if (paiementTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaiementTontineDTO> result = paiementTontineService.partialUpdate(paiementTontineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementTontineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paiement-tontines} : get all the paiementTontines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiementTontines in body.
     */
    @GetMapping("/paiement-tontines")
    public ResponseEntity<List<PaiementTontineDTO>> getAllPaiementTontines(
        PaiementTontineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PaiementTontines by criteria: {}", criteria);
        Page<PaiementTontineDTO> page = paiementTontineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiement-tontines/count} : count all the paiementTontines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/paiement-tontines/count")
    public ResponseEntity<Long> countPaiementTontines(PaiementTontineCriteria criteria) {
        log.debug("REST request to count PaiementTontines by criteria: {}", criteria);
        return ResponseEntity.ok().body(paiementTontineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /paiement-tontines/:id} : get the "id" paiementTontine.
     *
     * @param id the id of the paiementTontineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiementTontineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paiement-tontines/{id}")
    public ResponseEntity<PaiementTontineDTO> getPaiementTontine(@PathVariable Long id) {
        log.debug("REST request to get PaiementTontine : {}", id);
        Optional<PaiementTontineDTO> paiementTontineDTO = paiementTontineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiementTontineDTO);
    }

    /**
     * {@code DELETE  /paiement-tontines/:id} : delete the "id" paiementTontine.
     *
     * @param id the id of the paiementTontineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paiement-tontines/{id}")
    public ResponseEntity<Void> deletePaiementTontine(@PathVariable Long id) {
        log.debug("REST request to delete PaiementTontine : {}", id);
        paiementTontineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
