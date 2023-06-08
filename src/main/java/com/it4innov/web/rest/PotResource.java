package com.it4innov.web.rest;

import com.it4innov.repository.PotRepository;
import com.it4innov.service.PotQueryService;
import com.it4innov.service.PotService;
import com.it4innov.service.criteria.PotCriteria;
import com.it4innov.service.dto.PotDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Pot}.
 */
@RestController
@RequestMapping("/api")
public class PotResource {

    private final Logger log = LoggerFactory.getLogger(PotResource.class);

    private static final String ENTITY_NAME = "pot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PotService potService;

    private final PotRepository potRepository;

    private final PotQueryService potQueryService;

    public PotResource(PotService potService, PotRepository potRepository, PotQueryService potQueryService) {
        this.potService = potService;
        this.potRepository = potRepository;
        this.potQueryService = potQueryService;
    }

    /**
     * {@code POST  /pots} : Create a new pot.
     *
     * @param potDTO the potDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new potDTO, or with status {@code 400 (Bad Request)} if the pot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pots")
    public ResponseEntity<PotDTO> createPot(@Valid @RequestBody PotDTO potDTO) throws URISyntaxException {
        log.debug("REST request to save Pot : {}", potDTO);
        if (potDTO.getId() != null) {
            throw new BadRequestAlertException("A new pot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PotDTO result = potService.save(potDTO);
        return ResponseEntity
            .created(new URI("/api/pots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pots/:id} : Updates an existing pot.
     *
     * @param id the id of the potDTO to save.
     * @param potDTO the potDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated potDTO,
     * or with status {@code 400 (Bad Request)} if the potDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the potDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pots/{id}")
    public ResponseEntity<PotDTO> updatePot(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody PotDTO potDTO)
        throws URISyntaxException {
        log.debug("REST request to update Pot : {}, {}", id, potDTO);
        if (potDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, potDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!potRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PotDTO result = potService.update(potDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, potDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pots/:id} : Partial updates given fields of an existing pot, field will ignore if it is null
     *
     * @param id the id of the potDTO to save.
     * @param potDTO the potDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated potDTO,
     * or with status {@code 400 (Bad Request)} if the potDTO is not valid,
     * or with status {@code 404 (Not Found)} if the potDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the potDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PotDTO> partialUpdatePot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PotDTO potDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pot partially : {}, {}", id, potDTO);
        if (potDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, potDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!potRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PotDTO> result = potService.partialUpdate(potDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, potDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pots} : get all the pots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pots in body.
     */
    @GetMapping("/pots")
    public ResponseEntity<List<PotDTO>> getAllPots(PotCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Pots by criteria: {}", criteria);
        Page<PotDTO> page = potQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pots/count} : count all the pots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pots/count")
    public ResponseEntity<Long> countPots(PotCriteria criteria) {
        log.debug("REST request to count Pots by criteria: {}", criteria);
        return ResponseEntity.ok().body(potQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pots/:id} : get the "id" pot.
     *
     * @param id the id of the potDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the potDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pots/{id}")
    public ResponseEntity<PotDTO> getPot(@PathVariable Long id) {
        log.debug("REST request to get Pot : {}", id);
        Optional<PotDTO> potDTO = potService.findOne(id);
        return ResponseUtil.wrapOrNotFound(potDTO);
    }

    /**
     * {@code DELETE  /pots/:id} : delete the "id" pot.
     *
     * @param id the id of the potDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pots/{id}")
    public ResponseEntity<Void> deletePot(@PathVariable Long id) {
        log.debug("REST request to delete Pot : {}", id);
        potService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
