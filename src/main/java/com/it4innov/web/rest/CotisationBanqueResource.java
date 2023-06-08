package com.it4innov.web.rest;

import com.it4innov.repository.CotisationBanqueRepository;
import com.it4innov.service.CotisationBanqueQueryService;
import com.it4innov.service.CotisationBanqueService;
import com.it4innov.service.criteria.CotisationBanqueCriteria;
import com.it4innov.service.dto.CotisationBanqueDTO;
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
 * REST controller for managing {@link com.it4innov.domain.CotisationBanque}.
 */
@RestController
@RequestMapping("/api")
public class CotisationBanqueResource {

    private final Logger log = LoggerFactory.getLogger(CotisationBanqueResource.class);

    private static final String ENTITY_NAME = "cotisationBanque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CotisationBanqueService cotisationBanqueService;

    private final CotisationBanqueRepository cotisationBanqueRepository;

    private final CotisationBanqueQueryService cotisationBanqueQueryService;

    public CotisationBanqueResource(
        CotisationBanqueService cotisationBanqueService,
        CotisationBanqueRepository cotisationBanqueRepository,
        CotisationBanqueQueryService cotisationBanqueQueryService
    ) {
        this.cotisationBanqueService = cotisationBanqueService;
        this.cotisationBanqueRepository = cotisationBanqueRepository;
        this.cotisationBanqueQueryService = cotisationBanqueQueryService;
    }

    /**
     * {@code POST  /cotisation-banques} : Create a new cotisationBanque.
     *
     * @param cotisationBanqueDTO the cotisationBanqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cotisationBanqueDTO, or with status {@code 400 (Bad Request)} if the cotisationBanque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cotisation-banques")
    public ResponseEntity<CotisationBanqueDTO> createCotisationBanque(@RequestBody CotisationBanqueDTO cotisationBanqueDTO)
        throws URISyntaxException {
        log.debug("REST request to save CotisationBanque : {}", cotisationBanqueDTO);
        if (cotisationBanqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new cotisationBanque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CotisationBanqueDTO result = cotisationBanqueService.save(cotisationBanqueDTO);
        return ResponseEntity
            .created(new URI("/api/cotisation-banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cotisation-banques/:id} : Updates an existing cotisationBanque.
     *
     * @param id the id of the cotisationBanqueDTO to save.
     * @param cotisationBanqueDTO the cotisationBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cotisationBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the cotisationBanqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cotisationBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cotisation-banques/{id}")
    public ResponseEntity<CotisationBanqueDTO> updateCotisationBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CotisationBanqueDTO cotisationBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CotisationBanque : {}, {}", id, cotisationBanqueDTO);
        if (cotisationBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cotisationBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cotisationBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CotisationBanqueDTO result = cotisationBanqueService.update(cotisationBanqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cotisationBanqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cotisation-banques/:id} : Partial updates given fields of an existing cotisationBanque, field will ignore if it is null
     *
     * @param id the id of the cotisationBanqueDTO to save.
     * @param cotisationBanqueDTO the cotisationBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cotisationBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the cotisationBanqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cotisationBanqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cotisationBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cotisation-banques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CotisationBanqueDTO> partialUpdateCotisationBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CotisationBanqueDTO cotisationBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CotisationBanque partially : {}, {}", id, cotisationBanqueDTO);
        if (cotisationBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cotisationBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cotisationBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CotisationBanqueDTO> result = cotisationBanqueService.partialUpdate(cotisationBanqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cotisationBanqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cotisation-banques} : get all the cotisationBanques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cotisationBanques in body.
     */
    @GetMapping("/cotisation-banques")
    public ResponseEntity<List<CotisationBanqueDTO>> getAllCotisationBanques(
        CotisationBanqueCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CotisationBanques by criteria: {}", criteria);
        Page<CotisationBanqueDTO> page = cotisationBanqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cotisation-banques/count} : count all the cotisationBanques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cotisation-banques/count")
    public ResponseEntity<Long> countCotisationBanques(CotisationBanqueCriteria criteria) {
        log.debug("REST request to count CotisationBanques by criteria: {}", criteria);
        return ResponseEntity.ok().body(cotisationBanqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cotisation-banques/:id} : get the "id" cotisationBanque.
     *
     * @param id the id of the cotisationBanqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cotisationBanqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cotisation-banques/{id}")
    public ResponseEntity<CotisationBanqueDTO> getCotisationBanque(@PathVariable Long id) {
        log.debug("REST request to get CotisationBanque : {}", id);
        Optional<CotisationBanqueDTO> cotisationBanqueDTO = cotisationBanqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cotisationBanqueDTO);
    }

    /**
     * {@code DELETE  /cotisation-banques/:id} : delete the "id" cotisationBanque.
     *
     * @param id the id of the cotisationBanqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cotisation-banques/{id}")
    public ResponseEntity<Void> deleteCotisationBanque(@PathVariable Long id) {
        log.debug("REST request to delete CotisationBanque : {}", id);
        cotisationBanqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
