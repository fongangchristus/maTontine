package com.it4innov.web.rest;

import com.it4innov.repository.PaiementAdhesionRepository;
import com.it4innov.service.PaiementAdhesionQueryService;
import com.it4innov.service.PaiementAdhesionService;
import com.it4innov.service.criteria.PaiementAdhesionCriteria;
import com.it4innov.service.dto.PaiementAdhesionDTO;
import com.it4innov.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.it4innov.domain.PaiementAdhesion}.
 */
@RestController
@RequestMapping("/api")
public class PaiementAdhesionResource {

    private final Logger log = LoggerFactory.getLogger(PaiementAdhesionResource.class);

    private static final String ENTITY_NAME = "paiementAdhesion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaiementAdhesionService paiementAdhesionService;

    private final PaiementAdhesionRepository paiementAdhesionRepository;

    private final PaiementAdhesionQueryService paiementAdhesionQueryService;

    public PaiementAdhesionResource(
        PaiementAdhesionService paiementAdhesionService,
        PaiementAdhesionRepository paiementAdhesionRepository,
        PaiementAdhesionQueryService paiementAdhesionQueryService
    ) {
        this.paiementAdhesionService = paiementAdhesionService;
        this.paiementAdhesionRepository = paiementAdhesionRepository;
        this.paiementAdhesionQueryService = paiementAdhesionQueryService;
    }

    /**
     * {@code POST  /paiement-adhesions} : Create a new paiementAdhesion.
     *
     * @param paiementAdhesionDTO the paiementAdhesionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiementAdhesionDTO, or with status {@code 400 (Bad Request)} if the paiementAdhesion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paiement-adhesions")
    public ResponseEntity<PaiementAdhesionDTO> createPaiementAdhesion(@Valid @RequestBody PaiementAdhesionDTO paiementAdhesionDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaiementAdhesion : {}", paiementAdhesionDTO);
        if (paiementAdhesionDTO.getId() != null) {
            throw new BadRequestAlertException("A new paiementAdhesion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaiementAdhesionDTO result = paiementAdhesionService.save(paiementAdhesionDTO);
        return ResponseEntity
            .created(new URI("/api/paiement-adhesions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiement-adhesions/:id} : Updates an existing paiementAdhesion.
     *
     * @param id the id of the paiementAdhesionDTO to save.
     * @param paiementAdhesionDTO the paiementAdhesionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementAdhesionDTO,
     * or with status {@code 400 (Bad Request)} if the paiementAdhesionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiementAdhesionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paiement-adhesions/{id}")
    public ResponseEntity<PaiementAdhesionDTO> updatePaiementAdhesion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaiementAdhesionDTO paiementAdhesionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaiementAdhesion : {}, {}", id, paiementAdhesionDTO);
        if (paiementAdhesionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementAdhesionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementAdhesionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaiementAdhesionDTO result = paiementAdhesionService.update(paiementAdhesionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementAdhesionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paiement-adhesions/:id} : Partial updates given fields of an existing paiementAdhesion, field will ignore if it is null
     *
     * @param id the id of the paiementAdhesionDTO to save.
     * @param paiementAdhesionDTO the paiementAdhesionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementAdhesionDTO,
     * or with status {@code 400 (Bad Request)} if the paiementAdhesionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paiementAdhesionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paiementAdhesionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paiement-adhesions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaiementAdhesionDTO> partialUpdatePaiementAdhesion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaiementAdhesionDTO paiementAdhesionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaiementAdhesion partially : {}, {}", id, paiementAdhesionDTO);
        if (paiementAdhesionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementAdhesionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementAdhesionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaiementAdhesionDTO> result = paiementAdhesionService.partialUpdate(paiementAdhesionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementAdhesionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paiement-adhesions} : get all the paiementAdhesions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiementAdhesions in body.
     */
    @GetMapping("/paiement-adhesions")
    public ResponseEntity<List<PaiementAdhesionDTO>> getAllPaiementAdhesions(
        PaiementAdhesionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PaiementAdhesions by criteria: {}", criteria);
        Page<PaiementAdhesionDTO> page = paiementAdhesionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiement-adhesions/count} : count all the paiementAdhesions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/paiement-adhesions/count")
    public ResponseEntity<Long> countPaiementAdhesions(PaiementAdhesionCriteria criteria) {
        log.debug("REST request to count PaiementAdhesions by criteria: {}", criteria);
        return ResponseEntity.ok().body(paiementAdhesionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /paiement-adhesions/:id} : get the "id" paiementAdhesion.
     *
     * @param id the id of the paiementAdhesionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiementAdhesionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paiement-adhesions/{id}")
    public ResponseEntity<PaiementAdhesionDTO> getPaiementAdhesion(@PathVariable Long id) {
        log.debug("REST request to get PaiementAdhesion : {}", id);
        Optional<PaiementAdhesionDTO> paiementAdhesionDTO = paiementAdhesionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiementAdhesionDTO);
    }

    /**
     * {@code DELETE  /paiement-adhesions/:id} : delete the "id" paiementAdhesion.
     *
     * @param id the id of the paiementAdhesionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paiement-adhesions/{id}")
    public ResponseEntity<Void> deletePaiementAdhesion(@PathVariable Long id) {
        log.debug("REST request to delete PaiementAdhesion : {}", id);
        paiementAdhesionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
