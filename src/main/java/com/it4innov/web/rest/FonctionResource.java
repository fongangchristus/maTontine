package com.it4innov.web.rest;

import com.it4innov.repository.FonctionRepository;
import com.it4innov.service.FonctionQueryService;
import com.it4innov.service.FonctionService;
import com.it4innov.service.criteria.FonctionCriteria;
import com.it4innov.service.dto.FonctionDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Fonction}.
 */
@RestController
@RequestMapping("/api")
public class FonctionResource {

    private final Logger log = LoggerFactory.getLogger(FonctionResource.class);

    private static final String ENTITY_NAME = "fonction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FonctionService fonctionService;

    private final FonctionRepository fonctionRepository;

    private final FonctionQueryService fonctionQueryService;

    public FonctionResource(
        FonctionService fonctionService,
        FonctionRepository fonctionRepository,
        FonctionQueryService fonctionQueryService
    ) {
        this.fonctionService = fonctionService;
        this.fonctionRepository = fonctionRepository;
        this.fonctionQueryService = fonctionQueryService;
    }

    /**
     * {@code POST  /fonctions} : Create a new fonction.
     *
     * @param fonctionDTO the fonctionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fonctionDTO, or with status {@code 400 (Bad Request)} if the fonction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fonctions")
    public ResponseEntity<FonctionDTO> createFonction(@RequestBody FonctionDTO fonctionDTO) throws URISyntaxException {
        log.debug("REST request to save Fonction : {}", fonctionDTO);
        if (fonctionDTO.getId() != null) {
            throw new BadRequestAlertException("A new fonction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FonctionDTO result = fonctionService.save(fonctionDTO);
        return ResponseEntity
            .created(new URI("/api/fonctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fonctions/:id} : Updates an existing fonction.
     *
     * @param id the id of the fonctionDTO to save.
     * @param fonctionDTO the fonctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fonctionDTO,
     * or with status {@code 400 (Bad Request)} if the fonctionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fonctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fonctions/{id}")
    public ResponseEntity<FonctionDTO> updateFonction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FonctionDTO fonctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fonction : {}, {}", id, fonctionDTO);
        if (fonctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fonctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fonctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FonctionDTO result = fonctionService.update(fonctionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fonctionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fonctions/:id} : Partial updates given fields of an existing fonction, field will ignore if it is null
     *
     * @param id the id of the fonctionDTO to save.
     * @param fonctionDTO the fonctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fonctionDTO,
     * or with status {@code 400 (Bad Request)} if the fonctionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fonctionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fonctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fonctions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FonctionDTO> partialUpdateFonction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FonctionDTO fonctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fonction partially : {}, {}", id, fonctionDTO);
        if (fonctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fonctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fonctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FonctionDTO> result = fonctionService.partialUpdate(fonctionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fonctionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fonctions} : get all the fonctions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fonctions in body.
     */
    @GetMapping("/fonctions")
    public ResponseEntity<List<FonctionDTO>> getAllFonctions(
        FonctionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Fonctions by criteria: {}", criteria);
        Page<FonctionDTO> page = fonctionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fonctions/count} : count all the fonctions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fonctions/count")
    public ResponseEntity<Long> countFonctions(FonctionCriteria criteria) {
        log.debug("REST request to count Fonctions by criteria: {}", criteria);
        return ResponseEntity.ok().body(fonctionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fonctions/:id} : get the "id" fonction.
     *
     * @param id the id of the fonctionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fonctionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fonctions/{id}")
    public ResponseEntity<FonctionDTO> getFonction(@PathVariable Long id) {
        log.debug("REST request to get Fonction : {}", id);
        Optional<FonctionDTO> fonctionDTO = fonctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fonctionDTO);
    }

    /**
     * {@code DELETE  /fonctions/:id} : delete the "id" fonction.
     *
     * @param id the id of the fonctionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fonctions/{id}")
    public ResponseEntity<Void> deleteFonction(@PathVariable Long id) {
        log.debug("REST request to delete Fonction : {}", id);
        fonctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
