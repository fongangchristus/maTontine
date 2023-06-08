package com.it4innov.web.rest;

import com.it4innov.repository.AssociationRepository;
import com.it4innov.service.AssociationQueryService;
import com.it4innov.service.AssociationService;
import com.it4innov.service.criteria.AssociationCriteria;
import com.it4innov.service.dto.AssociationDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Association}.
 */
@RestController
@RequestMapping("/api")
public class AssociationResource {

    private final Logger log = LoggerFactory.getLogger(AssociationResource.class);

    private static final String ENTITY_NAME = "association";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssociationService associationService;

    private final AssociationRepository associationRepository;

    private final AssociationQueryService associationQueryService;

    public AssociationResource(
        AssociationService associationService,
        AssociationRepository associationRepository,
        AssociationQueryService associationQueryService
    ) {
        this.associationService = associationService;
        this.associationRepository = associationRepository;
        this.associationQueryService = associationQueryService;
    }

    /**
     * {@code POST  /associations} : Create a new association.
     *
     * @param associationDTO the associationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new associationDTO, or with status {@code 400 (Bad Request)} if the association has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/associations")
    public ResponseEntity<AssociationDTO> createAssociation(@Valid @RequestBody AssociationDTO associationDTO) throws URISyntaxException {
        log.debug("REST request to save Association : {}", associationDTO);
        if (associationDTO.getId() != null) {
            throw new BadRequestAlertException("A new association cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssociationDTO result = associationService.save(associationDTO);
        return ResponseEntity
            .created(new URI("/api/associations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /associations/:id} : Updates an existing association.
     *
     * @param id the id of the associationDTO to save.
     * @param associationDTO the associationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associationDTO,
     * or with status {@code 400 (Bad Request)} if the associationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the associationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/associations/{id}")
    public ResponseEntity<AssociationDTO> updateAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssociationDTO associationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Association : {}, {}", id, associationDTO);
        if (associationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!associationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssociationDTO result = associationService.update(associationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, associationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /associations/:id} : Partial updates given fields of an existing association, field will ignore if it is null
     *
     * @param id the id of the associationDTO to save.
     * @param associationDTO the associationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associationDTO,
     * or with status {@code 400 (Bad Request)} if the associationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the associationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the associationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/associations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssociationDTO> partialUpdateAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssociationDTO associationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Association partially : {}, {}", id, associationDTO);
        if (associationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!associationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssociationDTO> result = associationService.partialUpdate(associationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, associationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /associations} : get all the associations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of associations in body.
     */
    @GetMapping("/associations")
    public ResponseEntity<List<AssociationDTO>> getAllAssociations(
        AssociationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Associations by criteria: {}", criteria);
        Page<AssociationDTO> page = associationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /associations/count} : count all the associations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/associations/count")
    public ResponseEntity<Long> countAssociations(AssociationCriteria criteria) {
        log.debug("REST request to count Associations by criteria: {}", criteria);
        return ResponseEntity.ok().body(associationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /associations/:id} : get the "id" association.
     *
     * @param id the id of the associationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the associationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/associations/{id}")
    public ResponseEntity<AssociationDTO> getAssociation(@PathVariable Long id) {
        log.debug("REST request to get Association : {}", id);
        Optional<AssociationDTO> associationDTO = associationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(associationDTO);
    }

    /**
     * {@code DELETE  /associations/:id} : delete the "id" association.
     *
     * @param id the id of the associationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/associations/{id}")
    public ResponseEntity<Void> deleteAssociation(@PathVariable Long id) {
        log.debug("REST request to delete Association : {}", id);
        associationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
