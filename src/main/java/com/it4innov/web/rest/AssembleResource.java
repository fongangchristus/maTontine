package com.it4innov.web.rest;

import com.it4innov.repository.AssembleRepository;
import com.it4innov.service.AssembleQueryService;
import com.it4innov.service.AssembleService;
import com.it4innov.service.criteria.AssembleCriteria;
import com.it4innov.service.dto.AssembleDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Assemble}.
 */
@RestController
@RequestMapping("/api")
public class AssembleResource {

    private final Logger log = LoggerFactory.getLogger(AssembleResource.class);

    private static final String ENTITY_NAME = "assemble";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssembleService assembleService;

    private final AssembleRepository assembleRepository;

    private final AssembleQueryService assembleQueryService;

    public AssembleResource(
        AssembleService assembleService,
        AssembleRepository assembleRepository,
        AssembleQueryService assembleQueryService
    ) {
        this.assembleService = assembleService;
        this.assembleRepository = assembleRepository;
        this.assembleQueryService = assembleQueryService;
    }

    /**
     * {@code POST  /assembles} : Create a new assemble.
     *
     * @param assembleDTO the assembleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assembleDTO, or with status {@code 400 (Bad Request)} if the assemble has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assembles")
    public ResponseEntity<AssembleDTO> createAssemble(@Valid @RequestBody AssembleDTO assembleDTO) throws URISyntaxException {
        log.debug("REST request to save Assemble : {}", assembleDTO);
        if (assembleDTO.getId() != null) {
            throw new BadRequestAlertException("A new assemble cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssembleDTO result = assembleService.save(assembleDTO);
        return ResponseEntity
            .created(new URI("/api/assembles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assembles/:id} : Updates an existing assemble.
     *
     * @param id the id of the assembleDTO to save.
     * @param assembleDTO the assembleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assembleDTO,
     * or with status {@code 400 (Bad Request)} if the assembleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assembleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assembles/{id}")
    public ResponseEntity<AssembleDTO> updateAssemble(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssembleDTO assembleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Assemble : {}, {}", id, assembleDTO);
        if (assembleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assembleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assembleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssembleDTO result = assembleService.update(assembleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assembleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assembles/:id} : Partial updates given fields of an existing assemble, field will ignore if it is null
     *
     * @param id the id of the assembleDTO to save.
     * @param assembleDTO the assembleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assembleDTO,
     * or with status {@code 400 (Bad Request)} if the assembleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assembleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assembleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assembles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssembleDTO> partialUpdateAssemble(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssembleDTO assembleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assemble partially : {}, {}", id, assembleDTO);
        if (assembleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assembleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assembleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssembleDTO> result = assembleService.partialUpdate(assembleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assembleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assembles} : get all the assembles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assembles in body.
     */
    @GetMapping("/assembles")
    public ResponseEntity<List<AssembleDTO>> getAllAssembles(
        AssembleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Assembles by criteria: {}", criteria);
        Page<AssembleDTO> page = assembleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assembles/count} : count all the assembles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/assembles/count")
    public ResponseEntity<Long> countAssembles(AssembleCriteria criteria) {
        log.debug("REST request to count Assembles by criteria: {}", criteria);
        return ResponseEntity.ok().body(assembleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assembles/:id} : get the "id" assemble.
     *
     * @param id the id of the assembleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assembleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assembles/{id}")
    public ResponseEntity<AssembleDTO> getAssemble(@PathVariable Long id) {
        log.debug("REST request to get Assemble : {}", id);
        Optional<AssembleDTO> assembleDTO = assembleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assembleDTO);
    }

    /**
     * {@code DELETE  /assembles/:id} : delete the "id" assemble.
     *
     * @param id the id of the assembleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assembles/{id}")
    public ResponseEntity<Void> deleteAssemble(@PathVariable Long id) {
        log.debug("REST request to delete Assemble : {}", id);
        assembleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
