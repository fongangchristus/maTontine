package com.it4innov.web.rest;

import com.it4innov.repository.FormuleAdhesionRepository;
import com.it4innov.service.FormuleAdhesionQueryService;
import com.it4innov.service.FormuleAdhesionService;
import com.it4innov.service.criteria.FormuleAdhesionCriteria;
import com.it4innov.service.dto.FormuleAdhesionDTO;
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
 * REST controller for managing {@link com.it4innov.domain.FormuleAdhesion}.
 */
@RestController
@RequestMapping("/api")
public class FormuleAdhesionResource {

    private final Logger log = LoggerFactory.getLogger(FormuleAdhesionResource.class);

    private static final String ENTITY_NAME = "formuleAdhesion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormuleAdhesionService formuleAdhesionService;

    private final FormuleAdhesionRepository formuleAdhesionRepository;

    private final FormuleAdhesionQueryService formuleAdhesionQueryService;

    public FormuleAdhesionResource(
        FormuleAdhesionService formuleAdhesionService,
        FormuleAdhesionRepository formuleAdhesionRepository,
        FormuleAdhesionQueryService formuleAdhesionQueryService
    ) {
        this.formuleAdhesionService = formuleAdhesionService;
        this.formuleAdhesionRepository = formuleAdhesionRepository;
        this.formuleAdhesionQueryService = formuleAdhesionQueryService;
    }

    /**
     * {@code POST  /formule-adhesions} : Create a new formuleAdhesion.
     *
     * @param formuleAdhesionDTO the formuleAdhesionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formuleAdhesionDTO, or with status {@code 400 (Bad Request)} if the formuleAdhesion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formule-adhesions")
    public ResponseEntity<FormuleAdhesionDTO> createFormuleAdhesion(@RequestBody FormuleAdhesionDTO formuleAdhesionDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormuleAdhesion : {}", formuleAdhesionDTO);
        if (formuleAdhesionDTO.getId() != null) {
            throw new BadRequestAlertException("A new formuleAdhesion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormuleAdhesionDTO result = formuleAdhesionService.save(formuleAdhesionDTO);
        return ResponseEntity
            .created(new URI("/api/formule-adhesions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formule-adhesions/:id} : Updates an existing formuleAdhesion.
     *
     * @param id the id of the formuleAdhesionDTO to save.
     * @param formuleAdhesionDTO the formuleAdhesionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formuleAdhesionDTO,
     * or with status {@code 400 (Bad Request)} if the formuleAdhesionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formuleAdhesionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formule-adhesions/{id}")
    public ResponseEntity<FormuleAdhesionDTO> updateFormuleAdhesion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormuleAdhesionDTO formuleAdhesionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormuleAdhesion : {}, {}", id, formuleAdhesionDTO);
        if (formuleAdhesionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formuleAdhesionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formuleAdhesionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormuleAdhesionDTO result = formuleAdhesionService.update(formuleAdhesionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formuleAdhesionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formule-adhesions/:id} : Partial updates given fields of an existing formuleAdhesion, field will ignore if it is null
     *
     * @param id the id of the formuleAdhesionDTO to save.
     * @param formuleAdhesionDTO the formuleAdhesionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formuleAdhesionDTO,
     * or with status {@code 400 (Bad Request)} if the formuleAdhesionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formuleAdhesionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formuleAdhesionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formule-adhesions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormuleAdhesionDTO> partialUpdateFormuleAdhesion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormuleAdhesionDTO formuleAdhesionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormuleAdhesion partially : {}, {}", id, formuleAdhesionDTO);
        if (formuleAdhesionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formuleAdhesionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formuleAdhesionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormuleAdhesionDTO> result = formuleAdhesionService.partialUpdate(formuleAdhesionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formuleAdhesionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formule-adhesions} : get all the formuleAdhesions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formuleAdhesions in body.
     */
    @GetMapping("/formule-adhesions")
    public ResponseEntity<List<FormuleAdhesionDTO>> getAllFormuleAdhesions(
        FormuleAdhesionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FormuleAdhesions by criteria: {}", criteria);
        Page<FormuleAdhesionDTO> page = formuleAdhesionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formule-adhesions/count} : count all the formuleAdhesions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/formule-adhesions/count")
    public ResponseEntity<Long> countFormuleAdhesions(FormuleAdhesionCriteria criteria) {
        log.debug("REST request to count FormuleAdhesions by criteria: {}", criteria);
        return ResponseEntity.ok().body(formuleAdhesionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /formule-adhesions/:id} : get the "id" formuleAdhesion.
     *
     * @param id the id of the formuleAdhesionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formuleAdhesionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formule-adhesions/{id}")
    public ResponseEntity<FormuleAdhesionDTO> getFormuleAdhesion(@PathVariable Long id) {
        log.debug("REST request to get FormuleAdhesion : {}", id);
        Optional<FormuleAdhesionDTO> formuleAdhesionDTO = formuleAdhesionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formuleAdhesionDTO);
    }

    /**
     * {@code DELETE  /formule-adhesions/:id} : delete the "id" formuleAdhesion.
     *
     * @param id the id of the formuleAdhesionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formule-adhesions/{id}")
    public ResponseEntity<Void> deleteFormuleAdhesion(@PathVariable Long id) {
        log.debug("REST request to delete FormuleAdhesion : {}", id);
        formuleAdhesionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
