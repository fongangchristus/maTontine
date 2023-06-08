package com.it4innov.web.rest;

import com.it4innov.repository.FonctionAdherentRepository;
import com.it4innov.service.FonctionAdherentQueryService;
import com.it4innov.service.FonctionAdherentService;
import com.it4innov.service.criteria.FonctionAdherentCriteria;
import com.it4innov.service.dto.FonctionAdherentDTO;
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
 * REST controller for managing {@link com.it4innov.domain.FonctionAdherent}.
 */
@RestController
@RequestMapping("/api")
public class FonctionAdherentResource {

    private final Logger log = LoggerFactory.getLogger(FonctionAdherentResource.class);

    private static final String ENTITY_NAME = "fonctionAdherent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FonctionAdherentService fonctionAdherentService;

    private final FonctionAdherentRepository fonctionAdherentRepository;

    private final FonctionAdherentQueryService fonctionAdherentQueryService;

    public FonctionAdherentResource(
        FonctionAdherentService fonctionAdherentService,
        FonctionAdherentRepository fonctionAdherentRepository,
        FonctionAdherentQueryService fonctionAdherentQueryService
    ) {
        this.fonctionAdherentService = fonctionAdherentService;
        this.fonctionAdherentRepository = fonctionAdherentRepository;
        this.fonctionAdherentQueryService = fonctionAdherentQueryService;
    }

    /**
     * {@code POST  /fonction-adherents} : Create a new fonctionAdherent.
     *
     * @param fonctionAdherentDTO the fonctionAdherentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fonctionAdherentDTO, or with status {@code 400 (Bad Request)} if the fonctionAdherent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fonction-adherents")
    public ResponseEntity<FonctionAdherentDTO> createFonctionAdherent(@Valid @RequestBody FonctionAdherentDTO fonctionAdherentDTO)
        throws URISyntaxException {
        log.debug("REST request to save FonctionAdherent : {}", fonctionAdherentDTO);
        if (fonctionAdherentDTO.getId() != null) {
            throw new BadRequestAlertException("A new fonctionAdherent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FonctionAdherentDTO result = fonctionAdherentService.save(fonctionAdherentDTO);
        return ResponseEntity
            .created(new URI("/api/fonction-adherents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fonction-adherents/:id} : Updates an existing fonctionAdherent.
     *
     * @param id the id of the fonctionAdherentDTO to save.
     * @param fonctionAdherentDTO the fonctionAdherentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fonctionAdherentDTO,
     * or with status {@code 400 (Bad Request)} if the fonctionAdherentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fonctionAdherentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fonction-adherents/{id}")
    public ResponseEntity<FonctionAdherentDTO> updateFonctionAdherent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FonctionAdherentDTO fonctionAdherentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FonctionAdherent : {}, {}", id, fonctionAdherentDTO);
        if (fonctionAdherentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fonctionAdherentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fonctionAdherentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FonctionAdherentDTO result = fonctionAdherentService.update(fonctionAdherentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fonctionAdherentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fonction-adherents/:id} : Partial updates given fields of an existing fonctionAdherent, field will ignore if it is null
     *
     * @param id the id of the fonctionAdherentDTO to save.
     * @param fonctionAdherentDTO the fonctionAdherentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fonctionAdherentDTO,
     * or with status {@code 400 (Bad Request)} if the fonctionAdherentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fonctionAdherentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fonctionAdherentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fonction-adherents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FonctionAdherentDTO> partialUpdateFonctionAdherent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FonctionAdherentDTO fonctionAdherentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FonctionAdherent partially : {}, {}", id, fonctionAdherentDTO);
        if (fonctionAdherentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fonctionAdherentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fonctionAdherentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FonctionAdherentDTO> result = fonctionAdherentService.partialUpdate(fonctionAdherentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fonctionAdherentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fonction-adherents} : get all the fonctionAdherents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fonctionAdherents in body.
     */
    @GetMapping("/fonction-adherents")
    public ResponseEntity<List<FonctionAdherentDTO>> getAllFonctionAdherents(
        FonctionAdherentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FonctionAdherents by criteria: {}", criteria);
        Page<FonctionAdherentDTO> page = fonctionAdherentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fonction-adherents/count} : count all the fonctionAdherents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fonction-adherents/count")
    public ResponseEntity<Long> countFonctionAdherents(FonctionAdherentCriteria criteria) {
        log.debug("REST request to count FonctionAdherents by criteria: {}", criteria);
        return ResponseEntity.ok().body(fonctionAdherentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fonction-adherents/:id} : get the "id" fonctionAdherent.
     *
     * @param id the id of the fonctionAdherentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fonctionAdherentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fonction-adherents/{id}")
    public ResponseEntity<FonctionAdherentDTO> getFonctionAdherent(@PathVariable Long id) {
        log.debug("REST request to get FonctionAdherent : {}", id);
        Optional<FonctionAdherentDTO> fonctionAdherentDTO = fonctionAdherentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fonctionAdherentDTO);
    }

    /**
     * {@code DELETE  /fonction-adherents/:id} : delete the "id" fonctionAdherent.
     *
     * @param id the id of the fonctionAdherentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fonction-adherents/{id}")
    public ResponseEntity<Void> deleteFonctionAdherent(@PathVariable Long id) {
        log.debug("REST request to delete FonctionAdherent : {}", id);
        fonctionAdherentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
