package com.it4innov.web.rest;

import com.it4innov.repository.CompteBanqueRepository;
import com.it4innov.service.CompteBanqueQueryService;
import com.it4innov.service.CompteBanqueService;
import com.it4innov.service.criteria.CompteBanqueCriteria;
import com.it4innov.service.dto.CompteBanqueDTO;
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
 * REST controller for managing {@link com.it4innov.domain.CompteBanque}.
 */
@RestController
@RequestMapping("/api")
public class CompteBanqueResource {

    private final Logger log = LoggerFactory.getLogger(CompteBanqueResource.class);

    private static final String ENTITY_NAME = "compteBanque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompteBanqueService compteBanqueService;

    private final CompteBanqueRepository compteBanqueRepository;

    private final CompteBanqueQueryService compteBanqueQueryService;

    public CompteBanqueResource(
        CompteBanqueService compteBanqueService,
        CompteBanqueRepository compteBanqueRepository,
        CompteBanqueQueryService compteBanqueQueryService
    ) {
        this.compteBanqueService = compteBanqueService;
        this.compteBanqueRepository = compteBanqueRepository;
        this.compteBanqueQueryService = compteBanqueQueryService;
    }

    /**
     * {@code POST  /compte-banques} : Create a new compteBanque.
     *
     * @param compteBanqueDTO the compteBanqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compteBanqueDTO, or with status {@code 400 (Bad Request)} if the compteBanque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compte-banques")
    public ResponseEntity<CompteBanqueDTO> createCompteBanque(@RequestBody CompteBanqueDTO compteBanqueDTO) throws URISyntaxException {
        log.debug("REST request to save CompteBanque : {}", compteBanqueDTO);
        if (compteBanqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new compteBanque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompteBanqueDTO result = compteBanqueService.save(compteBanqueDTO);
        return ResponseEntity
            .created(new URI("/api/compte-banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compte-banques/:id} : Updates an existing compteBanque.
     *
     * @param id the id of the compteBanqueDTO to save.
     * @param compteBanqueDTO the compteBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the compteBanqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compteBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compte-banques/{id}")
    public ResponseEntity<CompteBanqueDTO> updateCompteBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompteBanqueDTO compteBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompteBanque : {}, {}", id, compteBanqueDTO);
        if (compteBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompteBanqueDTO result = compteBanqueService.update(compteBanqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteBanqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compte-banques/:id} : Partial updates given fields of an existing compteBanque, field will ignore if it is null
     *
     * @param id the id of the compteBanqueDTO to save.
     * @param compteBanqueDTO the compteBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the compteBanqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compteBanqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compteBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compte-banques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompteBanqueDTO> partialUpdateCompteBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompteBanqueDTO compteBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompteBanque partially : {}, {}", id, compteBanqueDTO);
        if (compteBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompteBanqueDTO> result = compteBanqueService.partialUpdate(compteBanqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteBanqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compte-banques} : get all the compteBanques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compteBanques in body.
     */
    @GetMapping("/compte-banques")
    public ResponseEntity<List<CompteBanqueDTO>> getAllCompteBanques(
        CompteBanqueCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CompteBanques by criteria: {}", criteria);
        Page<CompteBanqueDTO> page = compteBanqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compte-banques/count} : count all the compteBanques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/compte-banques/count")
    public ResponseEntity<Long> countCompteBanques(CompteBanqueCriteria criteria) {
        log.debug("REST request to count CompteBanques by criteria: {}", criteria);
        return ResponseEntity.ok().body(compteBanqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /compte-banques/:id} : get the "id" compteBanque.
     *
     * @param id the id of the compteBanqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compteBanqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compte-banques/{id}")
    public ResponseEntity<CompteBanqueDTO> getCompteBanque(@PathVariable Long id) {
        log.debug("REST request to get CompteBanque : {}", id);
        Optional<CompteBanqueDTO> compteBanqueDTO = compteBanqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compteBanqueDTO);
    }

    /**
     * {@code DELETE  /compte-banques/:id} : delete the "id" compteBanque.
     *
     * @param id the id of the compteBanqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compte-banques/{id}")
    public ResponseEntity<Void> deleteCompteBanque(@PathVariable Long id) {
        log.debug("REST request to delete CompteBanque : {}", id);
        compteBanqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
