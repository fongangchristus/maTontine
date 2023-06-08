package com.it4innov.web.rest;

import com.it4innov.repository.SessionTontineRepository;
import com.it4innov.service.SessionTontineQueryService;
import com.it4innov.service.SessionTontineService;
import com.it4innov.service.criteria.SessionTontineCriteria;
import com.it4innov.service.dto.SessionTontineDTO;
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
 * REST controller for managing {@link com.it4innov.domain.SessionTontine}.
 */
@RestController
@RequestMapping("/api")
public class SessionTontineResource {

    private final Logger log = LoggerFactory.getLogger(SessionTontineResource.class);

    private static final String ENTITY_NAME = "sessionTontine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionTontineService sessionTontineService;

    private final SessionTontineRepository sessionTontineRepository;

    private final SessionTontineQueryService sessionTontineQueryService;

    public SessionTontineResource(
        SessionTontineService sessionTontineService,
        SessionTontineRepository sessionTontineRepository,
        SessionTontineQueryService sessionTontineQueryService
    ) {
        this.sessionTontineService = sessionTontineService;
        this.sessionTontineRepository = sessionTontineRepository;
        this.sessionTontineQueryService = sessionTontineQueryService;
    }

    /**
     * {@code POST  /session-tontines} : Create a new sessionTontine.
     *
     * @param sessionTontineDTO the sessionTontineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionTontineDTO, or with status {@code 400 (Bad Request)} if the sessionTontine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/session-tontines")
    public ResponseEntity<SessionTontineDTO> createSessionTontine(@RequestBody SessionTontineDTO sessionTontineDTO)
        throws URISyntaxException {
        log.debug("REST request to save SessionTontine : {}", sessionTontineDTO);
        if (sessionTontineDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionTontine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionTontineDTO result = sessionTontineService.save(sessionTontineDTO);
        return ResponseEntity
            .created(new URI("/api/session-tontines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-tontines/:id} : Updates an existing sessionTontine.
     *
     * @param id the id of the sessionTontineDTO to save.
     * @param sessionTontineDTO the sessionTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionTontineDTO,
     * or with status {@code 400 (Bad Request)} if the sessionTontineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/session-tontines/{id}")
    public ResponseEntity<SessionTontineDTO> updateSessionTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionTontineDTO sessionTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SessionTontine : {}, {}", id, sessionTontineDTO);
        if (sessionTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionTontineDTO result = sessionTontineService.update(sessionTontineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionTontineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /session-tontines/:id} : Partial updates given fields of an existing sessionTontine, field will ignore if it is null
     *
     * @param id the id of the sessionTontineDTO to save.
     * @param sessionTontineDTO the sessionTontineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionTontineDTO,
     * or with status {@code 400 (Bad Request)} if the sessionTontineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionTontineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionTontineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/session-tontines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionTontineDTO> partialUpdateSessionTontine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionTontineDTO sessionTontineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionTontine partially : {}, {}", id, sessionTontineDTO);
        if (sessionTontineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionTontineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionTontineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionTontineDTO> result = sessionTontineService.partialUpdate(sessionTontineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionTontineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /session-tontines} : get all the sessionTontines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionTontines in body.
     */
    @GetMapping("/session-tontines")
    public ResponseEntity<List<SessionTontineDTO>> getAllSessionTontines(
        SessionTontineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SessionTontines by criteria: {}", criteria);
        Page<SessionTontineDTO> page = sessionTontineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /session-tontines/count} : count all the sessionTontines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/session-tontines/count")
    public ResponseEntity<Long> countSessionTontines(SessionTontineCriteria criteria) {
        log.debug("REST request to count SessionTontines by criteria: {}", criteria);
        return ResponseEntity.ok().body(sessionTontineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /session-tontines/:id} : get the "id" sessionTontine.
     *
     * @param id the id of the sessionTontineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionTontineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/session-tontines/{id}")
    public ResponseEntity<SessionTontineDTO> getSessionTontine(@PathVariable Long id) {
        log.debug("REST request to get SessionTontine : {}", id);
        Optional<SessionTontineDTO> sessionTontineDTO = sessionTontineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionTontineDTO);
    }

    /**
     * {@code DELETE  /session-tontines/:id} : delete the "id" sessionTontine.
     *
     * @param id the id of the sessionTontineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/session-tontines/{id}")
    public ResponseEntity<Void> deleteSessionTontine(@PathVariable Long id) {
        log.debug("REST request to delete SessionTontine : {}", id);
        sessionTontineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
