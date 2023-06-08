package com.it4innov.web.rest;

import com.it4innov.repository.DecaisementBanqueRepository;
import com.it4innov.service.DecaisementBanqueQueryService;
import com.it4innov.service.DecaisementBanqueService;
import com.it4innov.service.criteria.DecaisementBanqueCriteria;
import com.it4innov.service.dto.DecaisementBanqueDTO;
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
 * REST controller for managing {@link com.it4innov.domain.DecaisementBanque}.
 */
@RestController
@RequestMapping("/api")
public class DecaisementBanqueResource {

    private final Logger log = LoggerFactory.getLogger(DecaisementBanqueResource.class);

    private static final String ENTITY_NAME = "decaisementBanque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecaisementBanqueService decaisementBanqueService;

    private final DecaisementBanqueRepository decaisementBanqueRepository;

    private final DecaisementBanqueQueryService decaisementBanqueQueryService;

    public DecaisementBanqueResource(
        DecaisementBanqueService decaisementBanqueService,
        DecaisementBanqueRepository decaisementBanqueRepository,
        DecaisementBanqueQueryService decaisementBanqueQueryService
    ) {
        this.decaisementBanqueService = decaisementBanqueService;
        this.decaisementBanqueRepository = decaisementBanqueRepository;
        this.decaisementBanqueQueryService = decaisementBanqueQueryService;
    }

    /**
     * {@code POST  /decaisement-banques} : Create a new decaisementBanque.
     *
     * @param decaisementBanqueDTO the decaisementBanqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decaisementBanqueDTO, or with status {@code 400 (Bad Request)} if the decaisementBanque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/decaisement-banques")
    public ResponseEntity<DecaisementBanqueDTO> createDecaisementBanque(@RequestBody DecaisementBanqueDTO decaisementBanqueDTO)
        throws URISyntaxException {
        log.debug("REST request to save DecaisementBanque : {}", decaisementBanqueDTO);
        if (decaisementBanqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new decaisementBanque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DecaisementBanqueDTO result = decaisementBanqueService.save(decaisementBanqueDTO);
        return ResponseEntity
            .created(new URI("/api/decaisement-banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /decaisement-banques/:id} : Updates an existing decaisementBanque.
     *
     * @param id the id of the decaisementBanqueDTO to save.
     * @param decaisementBanqueDTO the decaisementBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decaisementBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the decaisementBanqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decaisementBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/decaisement-banques/{id}")
    public ResponseEntity<DecaisementBanqueDTO> updateDecaisementBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DecaisementBanqueDTO decaisementBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DecaisementBanque : {}, {}", id, decaisementBanqueDTO);
        if (decaisementBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decaisementBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decaisementBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DecaisementBanqueDTO result = decaisementBanqueService.update(decaisementBanqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decaisementBanqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /decaisement-banques/:id} : Partial updates given fields of an existing decaisementBanque, field will ignore if it is null
     *
     * @param id the id of the decaisementBanqueDTO to save.
     * @param decaisementBanqueDTO the decaisementBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decaisementBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the decaisementBanqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the decaisementBanqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the decaisementBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/decaisement-banques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DecaisementBanqueDTO> partialUpdateDecaisementBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DecaisementBanqueDTO decaisementBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DecaisementBanque partially : {}, {}", id, decaisementBanqueDTO);
        if (decaisementBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decaisementBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decaisementBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DecaisementBanqueDTO> result = decaisementBanqueService.partialUpdate(decaisementBanqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decaisementBanqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /decaisement-banques} : get all the decaisementBanques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decaisementBanques in body.
     */
    @GetMapping("/decaisement-banques")
    public ResponseEntity<List<DecaisementBanqueDTO>> getAllDecaisementBanques(
        DecaisementBanqueCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DecaisementBanques by criteria: {}", criteria);
        Page<DecaisementBanqueDTO> page = decaisementBanqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /decaisement-banques/count} : count all the decaisementBanques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/decaisement-banques/count")
    public ResponseEntity<Long> countDecaisementBanques(DecaisementBanqueCriteria criteria) {
        log.debug("REST request to count DecaisementBanques by criteria: {}", criteria);
        return ResponseEntity.ok().body(decaisementBanqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /decaisement-banques/:id} : get the "id" decaisementBanque.
     *
     * @param id the id of the decaisementBanqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decaisementBanqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/decaisement-banques/{id}")
    public ResponseEntity<DecaisementBanqueDTO> getDecaisementBanque(@PathVariable Long id) {
        log.debug("REST request to get DecaisementBanque : {}", id);
        Optional<DecaisementBanqueDTO> decaisementBanqueDTO = decaisementBanqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decaisementBanqueDTO);
    }

    /**
     * {@code DELETE  /decaisement-banques/:id} : delete the "id" decaisementBanque.
     *
     * @param id the id of the decaisementBanqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/decaisement-banques/{id}")
    public ResponseEntity<Void> deleteDecaisementBanque(@PathVariable Long id) {
        log.debug("REST request to delete DecaisementBanque : {}", id);
        decaisementBanqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
