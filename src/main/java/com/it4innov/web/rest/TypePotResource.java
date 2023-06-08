package com.it4innov.web.rest;

import com.it4innov.repository.TypePotRepository;
import com.it4innov.service.TypePotQueryService;
import com.it4innov.service.TypePotService;
import com.it4innov.service.criteria.TypePotCriteria;
import com.it4innov.service.dto.TypePotDTO;
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
 * REST controller for managing {@link com.it4innov.domain.TypePot}.
 */
@RestController
@RequestMapping("/api")
public class TypePotResource {

    private final Logger log = LoggerFactory.getLogger(TypePotResource.class);

    private static final String ENTITY_NAME = "typePot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePotService typePotService;

    private final TypePotRepository typePotRepository;

    private final TypePotQueryService typePotQueryService;

    public TypePotResource(TypePotService typePotService, TypePotRepository typePotRepository, TypePotQueryService typePotQueryService) {
        this.typePotService = typePotService;
        this.typePotRepository = typePotRepository;
        this.typePotQueryService = typePotQueryService;
    }

    /**
     * {@code POST  /type-pots} : Create a new typePot.
     *
     * @param typePotDTO the typePotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePotDTO, or with status {@code 400 (Bad Request)} if the typePot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-pots")
    public ResponseEntity<TypePotDTO> createTypePot(@Valid @RequestBody TypePotDTO typePotDTO) throws URISyntaxException {
        log.debug("REST request to save TypePot : {}", typePotDTO);
        if (typePotDTO.getId() != null) {
            throw new BadRequestAlertException("A new typePot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypePotDTO result = typePotService.save(typePotDTO);
        return ResponseEntity
            .created(new URI("/api/type-pots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-pots/:id} : Updates an existing typePot.
     *
     * @param id the id of the typePotDTO to save.
     * @param typePotDTO the typePotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePotDTO,
     * or with status {@code 400 (Bad Request)} if the typePotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-pots/{id}")
    public ResponseEntity<TypePotDTO> updateTypePot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypePotDTO typePotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypePot : {}, {}", id, typePotDTO);
        if (typePotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypePotDTO result = typePotService.update(typePotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-pots/:id} : Partial updates given fields of an existing typePot, field will ignore if it is null
     *
     * @param id the id of the typePotDTO to save.
     * @param typePotDTO the typePotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePotDTO,
     * or with status {@code 400 (Bad Request)} if the typePotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typePotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typePotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-pots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypePotDTO> partialUpdateTypePot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypePotDTO typePotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypePot partially : {}, {}", id, typePotDTO);
        if (typePotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypePotDTO> result = typePotService.partialUpdate(typePotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-pots} : get all the typePots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePots in body.
     */
    @GetMapping("/type-pots")
    public ResponseEntity<List<TypePotDTO>> getAllTypePots(
        TypePotCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TypePots by criteria: {}", criteria);
        Page<TypePotDTO> page = typePotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-pots/count} : count all the typePots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/type-pots/count")
    public ResponseEntity<Long> countTypePots(TypePotCriteria criteria) {
        log.debug("REST request to count TypePots by criteria: {}", criteria);
        return ResponseEntity.ok().body(typePotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-pots/:id} : get the "id" typePot.
     *
     * @param id the id of the typePotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-pots/{id}")
    public ResponseEntity<TypePotDTO> getTypePot(@PathVariable Long id) {
        log.debug("REST request to get TypePot : {}", id);
        Optional<TypePotDTO> typePotDTO = typePotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typePotDTO);
    }

    /**
     * {@code DELETE  /type-pots/:id} : delete the "id" typePot.
     *
     * @param id the id of the typePotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-pots/{id}")
    public ResponseEntity<Void> deleteTypePot(@PathVariable Long id) {
        log.debug("REST request to delete TypePot : {}", id);
        typePotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
