package com.it4innov.web.rest;

import com.it4innov.repository.AdhesionRepository;
import com.it4innov.service.AdhesionQueryService;
import com.it4innov.service.AdhesionService;
import com.it4innov.service.criteria.AdhesionCriteria;
import com.it4innov.service.dto.AdhesionDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Adhesion}.
 */
@RestController
@RequestMapping("/api")
public class AdhesionResource {

    private final Logger log = LoggerFactory.getLogger(AdhesionResource.class);

    private static final String ENTITY_NAME = "adhesion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdhesionService adhesionService;

    private final AdhesionRepository adhesionRepository;

    private final AdhesionQueryService adhesionQueryService;

    public AdhesionResource(
        AdhesionService adhesionService,
        AdhesionRepository adhesionRepository,
        AdhesionQueryService adhesionQueryService
    ) {
        this.adhesionService = adhesionService;
        this.adhesionRepository = adhesionRepository;
        this.adhesionQueryService = adhesionQueryService;
    }

    /**
     * {@code POST  /adhesions} : Create a new adhesion.
     *
     * @param adhesionDTO the adhesionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adhesionDTO, or with status {@code 400 (Bad Request)} if the adhesion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adhesions")
    public ResponseEntity<AdhesionDTO> createAdhesion(@RequestBody AdhesionDTO adhesionDTO) throws URISyntaxException {
        log.debug("REST request to save Adhesion : {}", adhesionDTO);
        if (adhesionDTO.getId() != null) {
            throw new BadRequestAlertException("A new adhesion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdhesionDTO result = adhesionService.save(adhesionDTO);
        return ResponseEntity
            .created(new URI("/api/adhesions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /adhesions/:id} : Updates an existing adhesion.
     *
     * @param id the id of the adhesionDTO to save.
     * @param adhesionDTO the adhesionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adhesionDTO,
     * or with status {@code 400 (Bad Request)} if the adhesionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adhesionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adhesions/{id}")
    public ResponseEntity<AdhesionDTO> updateAdhesion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdhesionDTO adhesionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Adhesion : {}, {}", id, adhesionDTO);
        if (adhesionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adhesionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adhesionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdhesionDTO result = adhesionService.update(adhesionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adhesionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /adhesions/:id} : Partial updates given fields of an existing adhesion, field will ignore if it is null
     *
     * @param id the id of the adhesionDTO to save.
     * @param adhesionDTO the adhesionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adhesionDTO,
     * or with status {@code 400 (Bad Request)} if the adhesionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adhesionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adhesionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/adhesions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdhesionDTO> partialUpdateAdhesion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdhesionDTO adhesionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Adhesion partially : {}, {}", id, adhesionDTO);
        if (adhesionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adhesionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adhesionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdhesionDTO> result = adhesionService.partialUpdate(adhesionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adhesionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /adhesions} : get all the adhesions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adhesions in body.
     */
    @GetMapping("/adhesions")
    public ResponseEntity<List<AdhesionDTO>> getAllAdhesions(
        AdhesionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Adhesions by criteria: {}", criteria);
        Page<AdhesionDTO> page = adhesionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /adhesions/count} : count all the adhesions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/adhesions/count")
    public ResponseEntity<Long> countAdhesions(AdhesionCriteria criteria) {
        log.debug("REST request to count Adhesions by criteria: {}", criteria);
        return ResponseEntity.ok().body(adhesionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /adhesions/:id} : get the "id" adhesion.
     *
     * @param id the id of the adhesionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adhesionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adhesions/{id}")
    public ResponseEntity<AdhesionDTO> getAdhesion(@PathVariable Long id) {
        log.debug("REST request to get Adhesion : {}", id);
        Optional<AdhesionDTO> adhesionDTO = adhesionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adhesionDTO);
    }

    /**
     * {@code DELETE  /adhesions/:id} : delete the "id" adhesion.
     *
     * @param id the id of the adhesionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adhesions/{id}")
    public ResponseEntity<Void> deleteAdhesion(@PathVariable Long id) {
        log.debug("REST request to delete Adhesion : {}", id);
        adhesionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
