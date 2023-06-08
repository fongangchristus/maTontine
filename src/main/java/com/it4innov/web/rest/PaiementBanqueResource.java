package com.it4innov.web.rest;

import com.it4innov.repository.PaiementBanqueRepository;
import com.it4innov.service.PaiementBanqueQueryService;
import com.it4innov.service.PaiementBanqueService;
import com.it4innov.service.criteria.PaiementBanqueCriteria;
import com.it4innov.service.dto.PaiementBanqueDTO;
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
 * REST controller for managing {@link com.it4innov.domain.PaiementBanque}.
 */
@RestController
@RequestMapping("/api")
public class PaiementBanqueResource {

    private final Logger log = LoggerFactory.getLogger(PaiementBanqueResource.class);

    private static final String ENTITY_NAME = "paiementBanque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaiementBanqueService paiementBanqueService;

    private final PaiementBanqueRepository paiementBanqueRepository;

    private final PaiementBanqueQueryService paiementBanqueQueryService;

    public PaiementBanqueResource(
        PaiementBanqueService paiementBanqueService,
        PaiementBanqueRepository paiementBanqueRepository,
        PaiementBanqueQueryService paiementBanqueQueryService
    ) {
        this.paiementBanqueService = paiementBanqueService;
        this.paiementBanqueRepository = paiementBanqueRepository;
        this.paiementBanqueQueryService = paiementBanqueQueryService;
    }

    /**
     * {@code POST  /paiement-banques} : Create a new paiementBanque.
     *
     * @param paiementBanqueDTO the paiementBanqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiementBanqueDTO, or with status {@code 400 (Bad Request)} if the paiementBanque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paiement-banques")
    public ResponseEntity<PaiementBanqueDTO> createPaiementBanque(@Valid @RequestBody PaiementBanqueDTO paiementBanqueDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaiementBanque : {}", paiementBanqueDTO);
        if (paiementBanqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new paiementBanque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaiementBanqueDTO result = paiementBanqueService.save(paiementBanqueDTO);
        return ResponseEntity
            .created(new URI("/api/paiement-banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiement-banques/:id} : Updates an existing paiementBanque.
     *
     * @param id the id of the paiementBanqueDTO to save.
     * @param paiementBanqueDTO the paiementBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the paiementBanqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiementBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paiement-banques/{id}")
    public ResponseEntity<PaiementBanqueDTO> updatePaiementBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaiementBanqueDTO paiementBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaiementBanque : {}, {}", id, paiementBanqueDTO);
        if (paiementBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaiementBanqueDTO result = paiementBanqueService.update(paiementBanqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementBanqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paiement-banques/:id} : Partial updates given fields of an existing paiementBanque, field will ignore if it is null
     *
     * @param id the id of the paiementBanqueDTO to save.
     * @param paiementBanqueDTO the paiementBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the paiementBanqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paiementBanqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paiementBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paiement-banques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaiementBanqueDTO> partialUpdatePaiementBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaiementBanqueDTO paiementBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaiementBanque partially : {}, {}", id, paiementBanqueDTO);
        if (paiementBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaiementBanqueDTO> result = paiementBanqueService.partialUpdate(paiementBanqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementBanqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paiement-banques} : get all the paiementBanques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiementBanques in body.
     */
    @GetMapping("/paiement-banques")
    public ResponseEntity<List<PaiementBanqueDTO>> getAllPaiementBanques(
        PaiementBanqueCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PaiementBanques by criteria: {}", criteria);
        Page<PaiementBanqueDTO> page = paiementBanqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiement-banques/count} : count all the paiementBanques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/paiement-banques/count")
    public ResponseEntity<Long> countPaiementBanques(PaiementBanqueCriteria criteria) {
        log.debug("REST request to count PaiementBanques by criteria: {}", criteria);
        return ResponseEntity.ok().body(paiementBanqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /paiement-banques/:id} : get the "id" paiementBanque.
     *
     * @param id the id of the paiementBanqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiementBanqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paiement-banques/{id}")
    public ResponseEntity<PaiementBanqueDTO> getPaiementBanque(@PathVariable Long id) {
        log.debug("REST request to get PaiementBanque : {}", id);
        Optional<PaiementBanqueDTO> paiementBanqueDTO = paiementBanqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiementBanqueDTO);
    }

    /**
     * {@code DELETE  /paiement-banques/:id} : delete the "id" paiementBanque.
     *
     * @param id the id of the paiementBanqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paiement-banques/{id}")
    public ResponseEntity<Void> deletePaiementBanque(@PathVariable Long id) {
        log.debug("REST request to delete PaiementBanque : {}", id);
        paiementBanqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
