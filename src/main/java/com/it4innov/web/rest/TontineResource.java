package com.it4innov.web.rest;

import com.it4innov.repository.TontineRepository;
import com.it4innov.service.TontineQueryService;
import com.it4innov.service.TontineService;
import com.it4innov.service.criteria.TontineCriteria;
import com.it4innov.service.dto.TontineDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Tontine}.
 */
@RestController
@RequestMapping("/api")
public class TontineResource {

    private final Logger log = LoggerFactory.getLogger(TontineResource.class);

    private static final String ENTITY_NAME = "tontine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TontineService tontineService;

    private final TontineRepository tontineRepository;

    private final TontineQueryService tontineQueryService;

    public TontineResource(TontineService tontineService, TontineRepository tontineRepository, TontineQueryService tontineQueryService) {
        this.tontineService = tontineService;
        this.tontineRepository = tontineRepository;
        this.tontineQueryService = tontineQueryService;
    }

    /**
     * {@code POST  /tontines} : Create a new tontine.
     *
     * @param tontineDTO the tontineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tontineDTO, or with status {@code 400 (Bad Request)} if the tontine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tontines")
    public ResponseEntity<TontineDTO> createTontine(@Valid @RequestBody TontineDTO tontineDTO) throws URISyntaxException {
        log.debug("REST request to save Tontine : {}", tontineDTO);
        if (tontineDTO.getId() != null) {
            throw new BadRequestAlertException("A new tontine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TontineDTO result = tontineService.save(tontineDTO);
        return ResponseEntity
            .created(new URI("/api/tontines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tontines/:id} : Updates an existing tontine.
     *
     * @param id the id of the tontineDTO to save.
     * @param tontineDTO the tontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tontineDTO,
     * or with status {@code 400 (Bad Request)} if the tontineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tontines/{id}")
    public ResponseEntity<TontineDTO> updateTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TontineDTO tontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tontine : {}, {}", id, tontineDTO);
        if (tontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TontineDTO result = tontineService.update(tontineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tontineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tontines/:id} : Partial updates given fields of an existing tontine, field will ignore if it is null
     *
     * @param id the id of the tontineDTO to save.
     * @param tontineDTO the tontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tontineDTO,
     * or with status {@code 400 (Bad Request)} if the tontineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tontineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tontines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TontineDTO> partialUpdateTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TontineDTO tontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tontine partially : {}, {}", id, tontineDTO);
        if (tontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TontineDTO> result = tontineService.partialUpdate(tontineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tontineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tontines} : get all the tontines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tontines in body.
     */
    @GetMapping("/tontines")
    public ResponseEntity<List<TontineDTO>> getAllTontines(
        TontineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Tontines by criteria: {}", criteria);
        Page<TontineDTO> page = tontineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tontines/count} : count all the tontines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tontines/count")
    public ResponseEntity<Long> countTontines(TontineCriteria criteria) {
        log.debug("REST request to count Tontines by criteria: {}", criteria);
        return ResponseEntity.ok().body(tontineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tontines/:id} : get the "id" tontine.
     *
     * @param id the id of the tontineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tontineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tontines/{id}")
    public ResponseEntity<TontineDTO> getTontine(@PathVariable Long id) {
        log.debug("REST request to get Tontine : {}", id);
        Optional<TontineDTO> tontineDTO = tontineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tontineDTO);
    }

    /**
     * {@code DELETE  /tontines/:id} : delete the "id" tontine.
     *
     * @param id the id of the tontineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tontines/{id}")
    public ResponseEntity<Void> deleteTontine(@PathVariable Long id) {
        log.debug("REST request to delete Tontine : {}", id);
        tontineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
