package com.it4innov.web.rest;

import com.it4innov.repository.CompteTontineRepository;
import com.it4innov.service.CompteTontineQueryService;
import com.it4innov.service.CompteTontineService;
import com.it4innov.service.criteria.CompteTontineCriteria;
import com.it4innov.service.dto.CompteTontineDTO;
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
 * REST controller for managing {@link com.it4innov.domain.CompteTontine}.
 */
@RestController
@RequestMapping("/api")
public class CompteTontineResource {

    private final Logger log = LoggerFactory.getLogger(CompteTontineResource.class);

    private static final String ENTITY_NAME = "compteTontine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompteTontineService compteTontineService;

    private final CompteTontineRepository compteTontineRepository;

    private final CompteTontineQueryService compteTontineQueryService;

    public CompteTontineResource(
        CompteTontineService compteTontineService,
        CompteTontineRepository compteTontineRepository,
        CompteTontineQueryService compteTontineQueryService
    ) {
        this.compteTontineService = compteTontineService;
        this.compteTontineRepository = compteTontineRepository;
        this.compteTontineQueryService = compteTontineQueryService;
    }

    /**
     * {@code POST  /compte-tontines} : Create a new compteTontine.
     *
     * @param compteTontineDTO the compteTontineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compteTontineDTO, or with status {@code 400 (Bad Request)} if the compteTontine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compte-tontines")
    public ResponseEntity<CompteTontineDTO> createCompteTontine(@Valid @RequestBody CompteTontineDTO compteTontineDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompteTontine : {}", compteTontineDTO);
        if (compteTontineDTO.getId() != null) {
            throw new BadRequestAlertException("A new compteTontine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompteTontineDTO result = compteTontineService.save(compteTontineDTO);
        return ResponseEntity
            .created(new URI("/api/compte-tontines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compte-tontines/:id} : Updates an existing compteTontine.
     *
     * @param id the id of the compteTontineDTO to save.
     * @param compteTontineDTO the compteTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteTontineDTO,
     * or with status {@code 400 (Bad Request)} if the compteTontineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compteTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compte-tontines/{id}")
    public ResponseEntity<CompteTontineDTO> updateCompteTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompteTontineDTO compteTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompteTontine : {}, {}", id, compteTontineDTO);
        if (compteTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompteTontineDTO result = compteTontineService.update(compteTontineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteTontineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compte-tontines/:id} : Partial updates given fields of an existing compteTontine, field will ignore if it is null
     *
     * @param id the id of the compteTontineDTO to save.
     * @param compteTontineDTO the compteTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteTontineDTO,
     * or with status {@code 400 (Bad Request)} if the compteTontineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compteTontineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compteTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compte-tontines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompteTontineDTO> partialUpdateCompteTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompteTontineDTO compteTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompteTontine partially : {}, {}", id, compteTontineDTO);
        if (compteTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompteTontineDTO> result = compteTontineService.partialUpdate(compteTontineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteTontineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compte-tontines} : get all the compteTontines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compteTontines in body.
     */
    @GetMapping("/compte-tontines")
    public ResponseEntity<List<CompteTontineDTO>> getAllCompteTontines(
        CompteTontineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CompteTontines by criteria: {}", criteria);
        Page<CompteTontineDTO> page = compteTontineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compte-tontines/count} : count all the compteTontines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/compte-tontines/count")
    public ResponseEntity<Long> countCompteTontines(CompteTontineCriteria criteria) {
        log.debug("REST request to count CompteTontines by criteria: {}", criteria);
        return ResponseEntity.ok().body(compteTontineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /compte-tontines/:id} : get the "id" compteTontine.
     *
     * @param id the id of the compteTontineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compteTontineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compte-tontines/{id}")
    public ResponseEntity<CompteTontineDTO> getCompteTontine(@PathVariable Long id) {
        log.debug("REST request to get CompteTontine : {}", id);
        Optional<CompteTontineDTO> compteTontineDTO = compteTontineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compteTontineDTO);
    }

    /**
     * {@code DELETE  /compte-tontines/:id} : delete the "id" compteTontine.
     *
     * @param id the id of the compteTontineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compte-tontines/{id}")
    public ResponseEntity<Void> deleteCompteTontine(@PathVariable Long id) {
        log.debug("REST request to delete CompteTontine : {}", id);
        compteTontineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
