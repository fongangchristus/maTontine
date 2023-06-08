package com.it4innov.web.rest;

import com.it4innov.repository.PaiementSanctionRepository;
import com.it4innov.service.PaiementSanctionQueryService;
import com.it4innov.service.PaiementSanctionService;
import com.it4innov.service.criteria.PaiementSanctionCriteria;
import com.it4innov.service.dto.PaiementSanctionDTO;
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
 * REST controller for managing {@link com.it4innov.domain.PaiementSanction}.
 */
@RestController
@RequestMapping("/api")
public class PaiementSanctionResource {

    private final Logger log = LoggerFactory.getLogger(PaiementSanctionResource.class);

    private static final String ENTITY_NAME = "paiementSanction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaiementSanctionService paiementSanctionService;

    private final PaiementSanctionRepository paiementSanctionRepository;

    private final PaiementSanctionQueryService paiementSanctionQueryService;

    public PaiementSanctionResource(
        PaiementSanctionService paiementSanctionService,
        PaiementSanctionRepository paiementSanctionRepository,
        PaiementSanctionQueryService paiementSanctionQueryService
    ) {
        this.paiementSanctionService = paiementSanctionService;
        this.paiementSanctionRepository = paiementSanctionRepository;
        this.paiementSanctionQueryService = paiementSanctionQueryService;
    }

    /**
     * {@code POST  /paiement-sanctions} : Create a new paiementSanction.
     *
     * @param paiementSanctionDTO the paiementSanctionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiementSanctionDTO, or with status {@code 400 (Bad Request)} if the paiementSanction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paiement-sanctions")
    public ResponseEntity<PaiementSanctionDTO> createPaiementSanction(@Valid @RequestBody PaiementSanctionDTO paiementSanctionDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaiementSanction : {}", paiementSanctionDTO);
        if (paiementSanctionDTO.getId() != null) {
            throw new BadRequestAlertException("A new paiementSanction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaiementSanctionDTO result = paiementSanctionService.save(paiementSanctionDTO);
        return ResponseEntity
            .created(new URI("/api/paiement-sanctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiement-sanctions/:id} : Updates an existing paiementSanction.
     *
     * @param id the id of the paiementSanctionDTO to save.
     * @param paiementSanctionDTO the paiementSanctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementSanctionDTO,
     * or with status {@code 400 (Bad Request)} if the paiementSanctionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiementSanctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paiement-sanctions/{id}")
    public ResponseEntity<PaiementSanctionDTO> updatePaiementSanction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaiementSanctionDTO paiementSanctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaiementSanction : {}, {}", id, paiementSanctionDTO);
        if (paiementSanctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementSanctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementSanctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaiementSanctionDTO result = paiementSanctionService.update(paiementSanctionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementSanctionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paiement-sanctions/:id} : Partial updates given fields of an existing paiementSanction, field will ignore if it is null
     *
     * @param id the id of the paiementSanctionDTO to save.
     * @param paiementSanctionDTO the paiementSanctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementSanctionDTO,
     * or with status {@code 400 (Bad Request)} if the paiementSanctionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paiementSanctionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paiementSanctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paiement-sanctions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaiementSanctionDTO> partialUpdatePaiementSanction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaiementSanctionDTO paiementSanctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaiementSanction partially : {}, {}", id, paiementSanctionDTO);
        if (paiementSanctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementSanctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementSanctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaiementSanctionDTO> result = paiementSanctionService.partialUpdate(paiementSanctionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementSanctionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paiement-sanctions} : get all the paiementSanctions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiementSanctions in body.
     */
    @GetMapping("/paiement-sanctions")
    public ResponseEntity<List<PaiementSanctionDTO>> getAllPaiementSanctions(
        PaiementSanctionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PaiementSanctions by criteria: {}", criteria);
        Page<PaiementSanctionDTO> page = paiementSanctionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiement-sanctions/count} : count all the paiementSanctions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/paiement-sanctions/count")
    public ResponseEntity<Long> countPaiementSanctions(PaiementSanctionCriteria criteria) {
        log.debug("REST request to count PaiementSanctions by criteria: {}", criteria);
        return ResponseEntity.ok().body(paiementSanctionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /paiement-sanctions/:id} : get the "id" paiementSanction.
     *
     * @param id the id of the paiementSanctionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiementSanctionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paiement-sanctions/{id}")
    public ResponseEntity<PaiementSanctionDTO> getPaiementSanction(@PathVariable Long id) {
        log.debug("REST request to get PaiementSanction : {}", id);
        Optional<PaiementSanctionDTO> paiementSanctionDTO = paiementSanctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiementSanctionDTO);
    }

    /**
     * {@code DELETE  /paiement-sanctions/:id} : delete the "id" paiementSanction.
     *
     * @param id the id of the paiementSanctionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paiement-sanctions/{id}")
    public ResponseEntity<Void> deletePaiementSanction(@PathVariable Long id) {
        log.debug("REST request to delete PaiementSanction : {}", id);
        paiementSanctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
