package com.it4innov.web.rest;

import com.it4innov.repository.SanctionConfigurationRepository;
import com.it4innov.service.SanctionConfigurationQueryService;
import com.it4innov.service.SanctionConfigurationService;
import com.it4innov.service.criteria.SanctionConfigurationCriteria;
import com.it4innov.service.dto.SanctionConfigurationDTO;
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
 * REST controller for managing {@link com.it4innov.domain.SanctionConfiguration}.
 */
@RestController
@RequestMapping("/api")
public class SanctionConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(SanctionConfigurationResource.class);

    private static final String ENTITY_NAME = "sanctionConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SanctionConfigurationService sanctionConfigurationService;

    private final SanctionConfigurationRepository sanctionConfigurationRepository;

    private final SanctionConfigurationQueryService sanctionConfigurationQueryService;

    public SanctionConfigurationResource(
        SanctionConfigurationService sanctionConfigurationService,
        SanctionConfigurationRepository sanctionConfigurationRepository,
        SanctionConfigurationQueryService sanctionConfigurationQueryService
    ) {
        this.sanctionConfigurationService = sanctionConfigurationService;
        this.sanctionConfigurationRepository = sanctionConfigurationRepository;
        this.sanctionConfigurationQueryService = sanctionConfigurationQueryService;
    }

    /**
     * {@code POST  /sanction-configurations} : Create a new sanctionConfiguration.
     *
     * @param sanctionConfigurationDTO the sanctionConfigurationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sanctionConfigurationDTO, or with status {@code 400 (Bad Request)} if the sanctionConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sanction-configurations")
    public ResponseEntity<SanctionConfigurationDTO> createSanctionConfiguration(
        @Valid @RequestBody SanctionConfigurationDTO sanctionConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SanctionConfiguration : {}", sanctionConfigurationDTO);
        if (sanctionConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new sanctionConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SanctionConfigurationDTO result = sanctionConfigurationService.save(sanctionConfigurationDTO);
        return ResponseEntity
            .created(new URI("/api/sanction-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sanction-configurations/:id} : Updates an existing sanctionConfiguration.
     *
     * @param id the id of the sanctionConfigurationDTO to save.
     * @param sanctionConfigurationDTO the sanctionConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the sanctionConfigurationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sanctionConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sanction-configurations/{id}")
    public ResponseEntity<SanctionConfigurationDTO> updateSanctionConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SanctionConfigurationDTO sanctionConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SanctionConfiguration : {}, {}", id, sanctionConfigurationDTO);
        if (sanctionConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SanctionConfigurationDTO result = sanctionConfigurationService.update(sanctionConfigurationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanctionConfigurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sanction-configurations/:id} : Partial updates given fields of an existing sanctionConfiguration, field will ignore if it is null
     *
     * @param id the id of the sanctionConfigurationDTO to save.
     * @param sanctionConfigurationDTO the sanctionConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the sanctionConfigurationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sanctionConfigurationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sanctionConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sanction-configurations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SanctionConfigurationDTO> partialUpdateSanctionConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SanctionConfigurationDTO sanctionConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SanctionConfiguration partially : {}, {}", id, sanctionConfigurationDTO);
        if (sanctionConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SanctionConfigurationDTO> result = sanctionConfigurationService.partialUpdate(sanctionConfigurationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanctionConfigurationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sanction-configurations} : get all the sanctionConfigurations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sanctionConfigurations in body.
     */
    @GetMapping("/sanction-configurations")
    public ResponseEntity<List<SanctionConfigurationDTO>> getAllSanctionConfigurations(
        SanctionConfigurationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SanctionConfigurations by criteria: {}", criteria);
        Page<SanctionConfigurationDTO> page = sanctionConfigurationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sanction-configurations/count} : count all the sanctionConfigurations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sanction-configurations/count")
    public ResponseEntity<Long> countSanctionConfigurations(SanctionConfigurationCriteria criteria) {
        log.debug("REST request to count SanctionConfigurations by criteria: {}", criteria);
        return ResponseEntity.ok().body(sanctionConfigurationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sanction-configurations/:id} : get the "id" sanctionConfiguration.
     *
     * @param id the id of the sanctionConfigurationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sanctionConfigurationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sanction-configurations/{id}")
    public ResponseEntity<SanctionConfigurationDTO> getSanctionConfiguration(@PathVariable Long id) {
        log.debug("REST request to get SanctionConfiguration : {}", id);
        Optional<SanctionConfigurationDTO> sanctionConfigurationDTO = sanctionConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sanctionConfigurationDTO);
    }

    /**
     * {@code DELETE  /sanction-configurations/:id} : delete the "id" sanctionConfiguration.
     *
     * @param id the id of the sanctionConfigurationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sanction-configurations/{id}")
    public ResponseEntity<Void> deleteSanctionConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete SanctionConfiguration : {}", id);
        sanctionConfigurationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
