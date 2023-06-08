package com.it4innov.web.rest;

import com.it4innov.repository.SanctionRepository;
import com.it4innov.service.SanctionQueryService;
import com.it4innov.service.SanctionService;
import com.it4innov.service.criteria.SanctionCriteria;
import com.it4innov.service.dto.SanctionDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Sanction}.
 */
@RestController
@RequestMapping("/api")
public class SanctionResource {

    private final Logger log = LoggerFactory.getLogger(SanctionResource.class);

    private static final String ENTITY_NAME = "sanction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SanctionService sanctionService;

    private final SanctionRepository sanctionRepository;

    private final SanctionQueryService sanctionQueryService;

    public SanctionResource(
        SanctionService sanctionService,
        SanctionRepository sanctionRepository,
        SanctionQueryService sanctionQueryService
    ) {
        this.sanctionService = sanctionService;
        this.sanctionRepository = sanctionRepository;
        this.sanctionQueryService = sanctionQueryService;
    }

    /**
     * {@code POST  /sanctions} : Create a new sanction.
     *
     * @param sanctionDTO the sanctionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sanctionDTO, or with status {@code 400 (Bad Request)} if the sanction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sanctions")
    public ResponseEntity<SanctionDTO> createSanction(@Valid @RequestBody SanctionDTO sanctionDTO) throws URISyntaxException {
        log.debug("REST request to save Sanction : {}", sanctionDTO);
        if (sanctionDTO.getId() != null) {
            throw new BadRequestAlertException("A new sanction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SanctionDTO result = sanctionService.save(sanctionDTO);
        return ResponseEntity
            .created(new URI("/api/sanctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sanctions/:id} : Updates an existing sanction.
     *
     * @param id the id of the sanctionDTO to save.
     * @param sanctionDTO the sanctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionDTO,
     * or with status {@code 400 (Bad Request)} if the sanctionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sanctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sanctions/{id}")
    public ResponseEntity<SanctionDTO> updateSanction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SanctionDTO sanctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sanction : {}, {}", id, sanctionDTO);
        if (sanctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SanctionDTO result = sanctionService.update(sanctionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanctionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sanctions/:id} : Partial updates given fields of an existing sanction, field will ignore if it is null
     *
     * @param id the id of the sanctionDTO to save.
     * @param sanctionDTO the sanctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionDTO,
     * or with status {@code 400 (Bad Request)} if the sanctionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sanctionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sanctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sanctions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SanctionDTO> partialUpdateSanction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SanctionDTO sanctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sanction partially : {}, {}", id, sanctionDTO);
        if (sanctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SanctionDTO> result = sanctionService.partialUpdate(sanctionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanctionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sanctions} : get all the sanctions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sanctions in body.
     */
    @GetMapping("/sanctions")
    public ResponseEntity<List<SanctionDTO>> getAllSanctions(
        SanctionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Sanctions by criteria: {}", criteria);
        Page<SanctionDTO> page = sanctionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sanctions/count} : count all the sanctions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sanctions/count")
    public ResponseEntity<Long> countSanctions(SanctionCriteria criteria) {
        log.debug("REST request to count Sanctions by criteria: {}", criteria);
        return ResponseEntity.ok().body(sanctionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sanctions/:id} : get the "id" sanction.
     *
     * @param id the id of the sanctionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sanctionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sanctions/{id}")
    public ResponseEntity<SanctionDTO> getSanction(@PathVariable Long id) {
        log.debug("REST request to get Sanction : {}", id);
        Optional<SanctionDTO> sanctionDTO = sanctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sanctionDTO);
    }

    /**
     * {@code DELETE  /sanctions/:id} : delete the "id" sanction.
     *
     * @param id the id of the sanctionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sanctions/{id}")
    public ResponseEntity<Void> deleteSanction(@PathVariable Long id) {
        log.debug("REST request to delete Sanction : {}", id);
        sanctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
