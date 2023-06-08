package com.it4innov.web.rest;

import com.it4innov.repository.CompteRIBRepository;
import com.it4innov.service.CompteRIBQueryService;
import com.it4innov.service.CompteRIBService;
import com.it4innov.service.criteria.CompteRIBCriteria;
import com.it4innov.service.dto.CompteRIBDTO;
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
 * REST controller for managing {@link com.it4innov.domain.CompteRIB}.
 */
@RestController
@RequestMapping("/api")
public class CompteRIBResource {

    private final Logger log = LoggerFactory.getLogger(CompteRIBResource.class);

    private static final String ENTITY_NAME = "compteRIB";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompteRIBService compteRIBService;

    private final CompteRIBRepository compteRIBRepository;

    private final CompteRIBQueryService compteRIBQueryService;

    public CompteRIBResource(
        CompteRIBService compteRIBService,
        CompteRIBRepository compteRIBRepository,
        CompteRIBQueryService compteRIBQueryService
    ) {
        this.compteRIBService = compteRIBService;
        this.compteRIBRepository = compteRIBRepository;
        this.compteRIBQueryService = compteRIBQueryService;
    }

    /**
     * {@code POST  /compte-ribs} : Create a new compteRIB.
     *
     * @param compteRIBDTO the compteRIBDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compteRIBDTO, or with status {@code 400 (Bad Request)} if the compteRIB has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compte-ribs")
    public ResponseEntity<CompteRIBDTO> createCompteRIB(@RequestBody CompteRIBDTO compteRIBDTO) throws URISyntaxException {
        log.debug("REST request to save CompteRIB : {}", compteRIBDTO);
        if (compteRIBDTO.getId() != null) {
            throw new BadRequestAlertException("A new compteRIB cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompteRIBDTO result = compteRIBService.save(compteRIBDTO);
        return ResponseEntity
            .created(new URI("/api/compte-ribs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compte-ribs/:id} : Updates an existing compteRIB.
     *
     * @param id the id of the compteRIBDTO to save.
     * @param compteRIBDTO the compteRIBDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteRIBDTO,
     * or with status {@code 400 (Bad Request)} if the compteRIBDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compteRIBDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compte-ribs/{id}")
    public ResponseEntity<CompteRIBDTO> updateCompteRIB(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompteRIBDTO compteRIBDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompteRIB : {}, {}", id, compteRIBDTO);
        if (compteRIBDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteRIBDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteRIBRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompteRIBDTO result = compteRIBService.update(compteRIBDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteRIBDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compte-ribs/:id} : Partial updates given fields of an existing compteRIB, field will ignore if it is null
     *
     * @param id the id of the compteRIBDTO to save.
     * @param compteRIBDTO the compteRIBDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteRIBDTO,
     * or with status {@code 400 (Bad Request)} if the compteRIBDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compteRIBDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compteRIBDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compte-ribs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompteRIBDTO> partialUpdateCompteRIB(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompteRIBDTO compteRIBDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompteRIB partially : {}, {}", id, compteRIBDTO);
        if (compteRIBDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteRIBDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteRIBRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompteRIBDTO> result = compteRIBService.partialUpdate(compteRIBDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteRIBDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compte-ribs} : get all the compteRIBS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compteRIBS in body.
     */
    @GetMapping("/compte-ribs")
    public ResponseEntity<List<CompteRIBDTO>> getAllCompteRIBS(
        CompteRIBCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CompteRIBS by criteria: {}", criteria);
        Page<CompteRIBDTO> page = compteRIBQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compte-ribs/count} : count all the compteRIBS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/compte-ribs/count")
    public ResponseEntity<Long> countCompteRIBS(CompteRIBCriteria criteria) {
        log.debug("REST request to count CompteRIBS by criteria: {}", criteria);
        return ResponseEntity.ok().body(compteRIBQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /compte-ribs/:id} : get the "id" compteRIB.
     *
     * @param id the id of the compteRIBDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compteRIBDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compte-ribs/{id}")
    public ResponseEntity<CompteRIBDTO> getCompteRIB(@PathVariable Long id) {
        log.debug("REST request to get CompteRIB : {}", id);
        Optional<CompteRIBDTO> compteRIBDTO = compteRIBService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compteRIBDTO);
    }

    /**
     * {@code DELETE  /compte-ribs/:id} : delete the "id" compteRIB.
     *
     * @param id the id of the compteRIBDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compte-ribs/{id}")
    public ResponseEntity<Void> deleteCompteRIB(@PathVariable Long id) {
        log.debug("REST request to delete CompteRIB : {}", id);
        compteRIBService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
