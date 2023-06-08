package com.it4innov.web.rest;

import com.it4innov.repository.GestionnaireTontineRepository;
import com.it4innov.service.GestionnaireTontineQueryService;
import com.it4innov.service.GestionnaireTontineService;
import com.it4innov.service.criteria.GestionnaireTontineCriteria;
import com.it4innov.service.dto.GestionnaireTontineDTO;
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
 * REST controller for managing {@link com.it4innov.domain.GestionnaireTontine}.
 */
@RestController
@RequestMapping("/api")
public class GestionnaireTontineResource {

    private final Logger log = LoggerFactory.getLogger(GestionnaireTontineResource.class);

    private static final String ENTITY_NAME = "gestionnaireTontine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GestionnaireTontineService gestionnaireTontineService;

    private final GestionnaireTontineRepository gestionnaireTontineRepository;

    private final GestionnaireTontineQueryService gestionnaireTontineQueryService;

    public GestionnaireTontineResource(
        GestionnaireTontineService gestionnaireTontineService,
        GestionnaireTontineRepository gestionnaireTontineRepository,
        GestionnaireTontineQueryService gestionnaireTontineQueryService
    ) {
        this.gestionnaireTontineService = gestionnaireTontineService;
        this.gestionnaireTontineRepository = gestionnaireTontineRepository;
        this.gestionnaireTontineQueryService = gestionnaireTontineQueryService;
    }

    /**
     * {@code POST  /gestionnaire-tontines} : Create a new gestionnaireTontine.
     *
     * @param gestionnaireTontineDTO the gestionnaireTontineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gestionnaireTontineDTO, or with status {@code 400 (Bad Request)} if the gestionnaireTontine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gestionnaire-tontines")
    public ResponseEntity<GestionnaireTontineDTO> createGestionnaireTontine(@RequestBody GestionnaireTontineDTO gestionnaireTontineDTO)
        throws URISyntaxException {
        log.debug("REST request to save GestionnaireTontine : {}", gestionnaireTontineDTO);
        if (gestionnaireTontineDTO.getId() != null) {
            throw new BadRequestAlertException("A new gestionnaireTontine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GestionnaireTontineDTO result = gestionnaireTontineService.save(gestionnaireTontineDTO);
        return ResponseEntity
            .created(new URI("/api/gestionnaire-tontines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gestionnaire-tontines/:id} : Updates an existing gestionnaireTontine.
     *
     * @param id the id of the gestionnaireTontineDTO to save.
     * @param gestionnaireTontineDTO the gestionnaireTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gestionnaireTontineDTO,
     * or with status {@code 400 (Bad Request)} if the gestionnaireTontineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gestionnaireTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gestionnaire-tontines/{id}")
    public ResponseEntity<GestionnaireTontineDTO> updateGestionnaireTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GestionnaireTontineDTO gestionnaireTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GestionnaireTontine : {}, {}", id, gestionnaireTontineDTO);
        if (gestionnaireTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gestionnaireTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gestionnaireTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GestionnaireTontineDTO result = gestionnaireTontineService.update(gestionnaireTontineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gestionnaireTontineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gestionnaire-tontines/:id} : Partial updates given fields of an existing gestionnaireTontine, field will ignore if it is null
     *
     * @param id the id of the gestionnaireTontineDTO to save.
     * @param gestionnaireTontineDTO the gestionnaireTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gestionnaireTontineDTO,
     * or with status {@code 400 (Bad Request)} if the gestionnaireTontineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gestionnaireTontineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gestionnaireTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gestionnaire-tontines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GestionnaireTontineDTO> partialUpdateGestionnaireTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GestionnaireTontineDTO gestionnaireTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GestionnaireTontine partially : {}, {}", id, gestionnaireTontineDTO);
        if (gestionnaireTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gestionnaireTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gestionnaireTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GestionnaireTontineDTO> result = gestionnaireTontineService.partialUpdate(gestionnaireTontineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gestionnaireTontineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gestionnaire-tontines} : get all the gestionnaireTontines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gestionnaireTontines in body.
     */
    @GetMapping("/gestionnaire-tontines")
    public ResponseEntity<List<GestionnaireTontineDTO>> getAllGestionnaireTontines(
        GestionnaireTontineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GestionnaireTontines by criteria: {}", criteria);
        Page<GestionnaireTontineDTO> page = gestionnaireTontineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gestionnaire-tontines/count} : count all the gestionnaireTontines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gestionnaire-tontines/count")
    public ResponseEntity<Long> countGestionnaireTontines(GestionnaireTontineCriteria criteria) {
        log.debug("REST request to count GestionnaireTontines by criteria: {}", criteria);
        return ResponseEntity.ok().body(gestionnaireTontineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gestionnaire-tontines/:id} : get the "id" gestionnaireTontine.
     *
     * @param id the id of the gestionnaireTontineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gestionnaireTontineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gestionnaire-tontines/{id}")
    public ResponseEntity<GestionnaireTontineDTO> getGestionnaireTontine(@PathVariable Long id) {
        log.debug("REST request to get GestionnaireTontine : {}", id);
        Optional<GestionnaireTontineDTO> gestionnaireTontineDTO = gestionnaireTontineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gestionnaireTontineDTO);
    }

    /**
     * {@code DELETE  /gestionnaire-tontines/:id} : delete the "id" gestionnaireTontine.
     *
     * @param id the id of the gestionnaireTontineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gestionnaire-tontines/{id}")
    public ResponseEntity<Void> deleteGestionnaireTontine(@PathVariable Long id) {
        log.debug("REST request to delete GestionnaireTontine : {}", id);
        gestionnaireTontineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
