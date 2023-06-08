package com.it4innov.web.rest;

import com.it4innov.repository.DocumentAssociationRepository;
import com.it4innov.service.DocumentAssociationQueryService;
import com.it4innov.service.DocumentAssociationService;
import com.it4innov.service.criteria.DocumentAssociationCriteria;
import com.it4innov.service.dto.DocumentAssociationDTO;
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
 * REST controller for managing {@link com.it4innov.domain.DocumentAssociation}.
 */
@RestController
@RequestMapping("/api")
public class DocumentAssociationResource {

    private final Logger log = LoggerFactory.getLogger(DocumentAssociationResource.class);

    private static final String ENTITY_NAME = "documentAssociation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentAssociationService documentAssociationService;

    private final DocumentAssociationRepository documentAssociationRepository;

    private final DocumentAssociationQueryService documentAssociationQueryService;

    public DocumentAssociationResource(
        DocumentAssociationService documentAssociationService,
        DocumentAssociationRepository documentAssociationRepository,
        DocumentAssociationQueryService documentAssociationQueryService
    ) {
        this.documentAssociationService = documentAssociationService;
        this.documentAssociationRepository = documentAssociationRepository;
        this.documentAssociationQueryService = documentAssociationQueryService;
    }

    /**
     * {@code POST  /document-associations} : Create a new documentAssociation.
     *
     * @param documentAssociationDTO the documentAssociationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentAssociationDTO, or with status {@code 400 (Bad Request)} if the documentAssociation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-associations")
    public ResponseEntity<DocumentAssociationDTO> createDocumentAssociation(@RequestBody DocumentAssociationDTO documentAssociationDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocumentAssociation : {}", documentAssociationDTO);
        if (documentAssociationDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentAssociation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentAssociationDTO result = documentAssociationService.save(documentAssociationDTO);
        return ResponseEntity
            .created(new URI("/api/document-associations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-associations/:id} : Updates an existing documentAssociation.
     *
     * @param id the id of the documentAssociationDTO to save.
     * @param documentAssociationDTO the documentAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the documentAssociationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-associations/{id}")
    public ResponseEntity<DocumentAssociationDTO> updateDocumentAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentAssociationDTO documentAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentAssociation : {}, {}", id, documentAssociationDTO);
        if (documentAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentAssociationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentAssociationDTO result = documentAssociationService.update(documentAssociationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentAssociationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-associations/:id} : Partial updates given fields of an existing documentAssociation, field will ignore if it is null
     *
     * @param id the id of the documentAssociationDTO to save.
     * @param documentAssociationDTO the documentAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the documentAssociationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentAssociationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-associations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentAssociationDTO> partialUpdateDocumentAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentAssociationDTO documentAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentAssociation partially : {}, {}", id, documentAssociationDTO);
        if (documentAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentAssociationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentAssociationDTO> result = documentAssociationService.partialUpdate(documentAssociationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentAssociationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-associations} : get all the documentAssociations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentAssociations in body.
     */
    @GetMapping("/document-associations")
    public ResponseEntity<List<DocumentAssociationDTO>> getAllDocumentAssociations(
        DocumentAssociationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocumentAssociations by criteria: {}", criteria);
        Page<DocumentAssociationDTO> page = documentAssociationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-associations/count} : count all the documentAssociations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/document-associations/count")
    public ResponseEntity<Long> countDocumentAssociations(DocumentAssociationCriteria criteria) {
        log.debug("REST request to count DocumentAssociations by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentAssociationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-associations/:id} : get the "id" documentAssociation.
     *
     * @param id the id of the documentAssociationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentAssociationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-associations/{id}")
    public ResponseEntity<DocumentAssociationDTO> getDocumentAssociation(@PathVariable Long id) {
        log.debug("REST request to get DocumentAssociation : {}", id);
        Optional<DocumentAssociationDTO> documentAssociationDTO = documentAssociationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentAssociationDTO);
    }

    /**
     * {@code DELETE  /document-associations/:id} : delete the "id" documentAssociation.
     *
     * @param id the id of the documentAssociationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-associations/{id}")
    public ResponseEntity<Void> deleteDocumentAssociation(@PathVariable Long id) {
        log.debug("REST request to delete DocumentAssociation : {}", id);
        documentAssociationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
