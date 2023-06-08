package com.it4innov.web.rest;

import com.it4innov.repository.ContributionPotRepository;
import com.it4innov.service.ContributionPotQueryService;
import com.it4innov.service.ContributionPotService;
import com.it4innov.service.criteria.ContributionPotCriteria;
import com.it4innov.service.dto.ContributionPotDTO;
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
 * REST controller for managing {@link com.it4innov.domain.ContributionPot}.
 */
@RestController
@RequestMapping("/api")
public class ContributionPotResource {

    private final Logger log = LoggerFactory.getLogger(ContributionPotResource.class);

    private static final String ENTITY_NAME = "contributionPot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContributionPotService contributionPotService;

    private final ContributionPotRepository contributionPotRepository;

    private final ContributionPotQueryService contributionPotQueryService;

    public ContributionPotResource(
        ContributionPotService contributionPotService,
        ContributionPotRepository contributionPotRepository,
        ContributionPotQueryService contributionPotQueryService
    ) {
        this.contributionPotService = contributionPotService;
        this.contributionPotRepository = contributionPotRepository;
        this.contributionPotQueryService = contributionPotQueryService;
    }

    /**
     * {@code POST  /contribution-pots} : Create a new contributionPot.
     *
     * @param contributionPotDTO the contributionPotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contributionPotDTO, or with status {@code 400 (Bad Request)} if the contributionPot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contribution-pots")
    public ResponseEntity<ContributionPotDTO> createContributionPot(@Valid @RequestBody ContributionPotDTO contributionPotDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContributionPot : {}", contributionPotDTO);
        if (contributionPotDTO.getId() != null) {
            throw new BadRequestAlertException("A new contributionPot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributionPotDTO result = contributionPotService.save(contributionPotDTO);
        return ResponseEntity
            .created(new URI("/api/contribution-pots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contribution-pots/:id} : Updates an existing contributionPot.
     *
     * @param id the id of the contributionPotDTO to save.
     * @param contributionPotDTO the contributionPotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contributionPotDTO,
     * or with status {@code 400 (Bad Request)} if the contributionPotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contributionPotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contribution-pots/{id}")
    public ResponseEntity<ContributionPotDTO> updateContributionPot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContributionPotDTO contributionPotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContributionPot : {}, {}", id, contributionPotDTO);
        if (contributionPotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contributionPotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contributionPotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContributionPotDTO result = contributionPotService.update(contributionPotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contributionPotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contribution-pots/:id} : Partial updates given fields of an existing contributionPot, field will ignore if it is null
     *
     * @param id the id of the contributionPotDTO to save.
     * @param contributionPotDTO the contributionPotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contributionPotDTO,
     * or with status {@code 400 (Bad Request)} if the contributionPotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contributionPotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contributionPotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contribution-pots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContributionPotDTO> partialUpdateContributionPot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContributionPotDTO contributionPotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContributionPot partially : {}, {}", id, contributionPotDTO);
        if (contributionPotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contributionPotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contributionPotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContributionPotDTO> result = contributionPotService.partialUpdate(contributionPotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contributionPotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contribution-pots} : get all the contributionPots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contributionPots in body.
     */
    @GetMapping("/contribution-pots")
    public ResponseEntity<List<ContributionPotDTO>> getAllContributionPots(
        ContributionPotCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ContributionPots by criteria: {}", criteria);
        Page<ContributionPotDTO> page = contributionPotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contribution-pots/count} : count all the contributionPots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contribution-pots/count")
    public ResponseEntity<Long> countContributionPots(ContributionPotCriteria criteria) {
        log.debug("REST request to count ContributionPots by criteria: {}", criteria);
        return ResponseEntity.ok().body(contributionPotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contribution-pots/:id} : get the "id" contributionPot.
     *
     * @param id the id of the contributionPotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contributionPotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contribution-pots/{id}")
    public ResponseEntity<ContributionPotDTO> getContributionPot(@PathVariable Long id) {
        log.debug("REST request to get ContributionPot : {}", id);
        Optional<ContributionPotDTO> contributionPotDTO = contributionPotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contributionPotDTO);
    }

    /**
     * {@code DELETE  /contribution-pots/:id} : delete the "id" contributionPot.
     *
     * @param id the id of the contributionPotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contribution-pots/{id}")
    public ResponseEntity<Void> deleteContributionPot(@PathVariable Long id) {
        log.debug("REST request to delete ContributionPot : {}", id);
        contributionPotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
