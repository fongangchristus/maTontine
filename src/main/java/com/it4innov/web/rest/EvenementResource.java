package com.it4innov.web.rest;

import com.it4innov.repository.EvenementRepository;
import com.it4innov.service.EvenementQueryService;
import com.it4innov.service.EvenementService;
import com.it4innov.service.criteria.EvenementCriteria;
import com.it4innov.service.dto.EvenementDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Evenement}.
 */
@RestController
@RequestMapping("/api")
public class EvenementResource {

    private final Logger log = LoggerFactory.getLogger(EvenementResource.class);

    private static final String ENTITY_NAME = "evenement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvenementService evenementService;

    private final EvenementRepository evenementRepository;

    private final EvenementQueryService evenementQueryService;

    public EvenementResource(
        EvenementService evenementService,
        EvenementRepository evenementRepository,
        EvenementQueryService evenementQueryService
    ) {
        this.evenementService = evenementService;
        this.evenementRepository = evenementRepository;
        this.evenementQueryService = evenementQueryService;
    }

    /**
     * {@code POST  /evenements} : Create a new evenement.
     *
     * @param evenementDTO the evenementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evenementDTO, or with status {@code 400 (Bad Request)} if the evenement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evenements")
    public ResponseEntity<EvenementDTO> createEvenement(@RequestBody EvenementDTO evenementDTO) throws URISyntaxException {
        log.debug("REST request to save Evenement : {}", evenementDTO);
        if (evenementDTO.getId() != null) {
            throw new BadRequestAlertException("A new evenement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EvenementDTO result = evenementService.save(evenementDTO);
        return ResponseEntity
            .created(new URI("/api/evenements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evenements/:id} : Updates an existing evenement.
     *
     * @param id the id of the evenementDTO to save.
     * @param evenementDTO the evenementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evenementDTO,
     * or with status {@code 400 (Bad Request)} if the evenementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evenementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evenements/{id}")
    public ResponseEntity<EvenementDTO> updateEvenement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EvenementDTO evenementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Evenement : {}, {}", id, evenementDTO);
        if (evenementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evenementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evenementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EvenementDTO result = evenementService.update(evenementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evenementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evenements/:id} : Partial updates given fields of an existing evenement, field will ignore if it is null
     *
     * @param id the id of the evenementDTO to save.
     * @param evenementDTO the evenementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evenementDTO,
     * or with status {@code 400 (Bad Request)} if the evenementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the evenementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the evenementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evenements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvenementDTO> partialUpdateEvenement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EvenementDTO evenementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evenement partially : {}, {}", id, evenementDTO);
        if (evenementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evenementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evenementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvenementDTO> result = evenementService.partialUpdate(evenementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evenementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /evenements} : get all the evenements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evenements in body.
     */
    @GetMapping("/evenements")
    public ResponseEntity<List<EvenementDTO>> getAllEvenements(
        EvenementCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Evenements by criteria: {}", criteria);
        Page<EvenementDTO> page = evenementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evenements/count} : count all the evenements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/evenements/count")
    public ResponseEntity<Long> countEvenements(EvenementCriteria criteria) {
        log.debug("REST request to count Evenements by criteria: {}", criteria);
        return ResponseEntity.ok().body(evenementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evenements/:id} : get the "id" evenement.
     *
     * @param id the id of the evenementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evenementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evenements/{id}")
    public ResponseEntity<EvenementDTO> getEvenement(@PathVariable Long id) {
        log.debug("REST request to get Evenement : {}", id);
        Optional<EvenementDTO> evenementDTO = evenementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evenementDTO);
    }

    /**
     * {@code DELETE  /evenements/:id} : delete the "id" evenement.
     *
     * @param id the id of the evenementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evenements/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        log.debug("REST request to delete Evenement : {}", id);
        evenementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
