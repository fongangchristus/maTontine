package com.it4innov.web.rest;

import com.it4innov.repository.GestionnaireBanqueRepository;
import com.it4innov.service.GestionnaireBanqueQueryService;
import com.it4innov.service.GestionnaireBanqueService;
import com.it4innov.service.criteria.GestionnaireBanqueCriteria;
import com.it4innov.service.dto.GestionnaireBanqueDTO;
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
 * REST controller for managing {@link com.it4innov.domain.GestionnaireBanque}.
 */
@RestController
@RequestMapping("/api")
public class GestionnaireBanqueResource {

    private final Logger log = LoggerFactory.getLogger(GestionnaireBanqueResource.class);

    private static final String ENTITY_NAME = "gestionnaireBanque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GestionnaireBanqueService gestionnaireBanqueService;

    private final GestionnaireBanqueRepository gestionnaireBanqueRepository;

    private final GestionnaireBanqueQueryService gestionnaireBanqueQueryService;

    public GestionnaireBanqueResource(
        GestionnaireBanqueService gestionnaireBanqueService,
        GestionnaireBanqueRepository gestionnaireBanqueRepository,
        GestionnaireBanqueQueryService gestionnaireBanqueQueryService
    ) {
        this.gestionnaireBanqueService = gestionnaireBanqueService;
        this.gestionnaireBanqueRepository = gestionnaireBanqueRepository;
        this.gestionnaireBanqueQueryService = gestionnaireBanqueQueryService;
    }

    /**
     * {@code POST  /gestionnaire-banques} : Create a new gestionnaireBanque.
     *
     * @param gestionnaireBanqueDTO the gestionnaireBanqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gestionnaireBanqueDTO, or with status {@code 400 (Bad Request)} if the gestionnaireBanque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gestionnaire-banques")
    public ResponseEntity<GestionnaireBanqueDTO> createGestionnaireBanque(@RequestBody GestionnaireBanqueDTO gestionnaireBanqueDTO)
        throws URISyntaxException {
        log.debug("REST request to save GestionnaireBanque : {}", gestionnaireBanqueDTO);
        if (gestionnaireBanqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new gestionnaireBanque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GestionnaireBanqueDTO result = gestionnaireBanqueService.save(gestionnaireBanqueDTO);
        return ResponseEntity
            .created(new URI("/api/gestionnaire-banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gestionnaire-banques/:id} : Updates an existing gestionnaireBanque.
     *
     * @param id the id of the gestionnaireBanqueDTO to save.
     * @param gestionnaireBanqueDTO the gestionnaireBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gestionnaireBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the gestionnaireBanqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gestionnaireBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gestionnaire-banques/{id}")
    public ResponseEntity<GestionnaireBanqueDTO> updateGestionnaireBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GestionnaireBanqueDTO gestionnaireBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GestionnaireBanque : {}, {}", id, gestionnaireBanqueDTO);
        if (gestionnaireBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gestionnaireBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gestionnaireBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GestionnaireBanqueDTO result = gestionnaireBanqueService.update(gestionnaireBanqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gestionnaireBanqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gestionnaire-banques/:id} : Partial updates given fields of an existing gestionnaireBanque, field will ignore if it is null
     *
     * @param id the id of the gestionnaireBanqueDTO to save.
     * @param gestionnaireBanqueDTO the gestionnaireBanqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gestionnaireBanqueDTO,
     * or with status {@code 400 (Bad Request)} if the gestionnaireBanqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gestionnaireBanqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gestionnaireBanqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gestionnaire-banques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GestionnaireBanqueDTO> partialUpdateGestionnaireBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GestionnaireBanqueDTO gestionnaireBanqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GestionnaireBanque partially : {}, {}", id, gestionnaireBanqueDTO);
        if (gestionnaireBanqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gestionnaireBanqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gestionnaireBanqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GestionnaireBanqueDTO> result = gestionnaireBanqueService.partialUpdate(gestionnaireBanqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gestionnaireBanqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gestionnaire-banques} : get all the gestionnaireBanques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gestionnaireBanques in body.
     */
    @GetMapping("/gestionnaire-banques")
    public ResponseEntity<List<GestionnaireBanqueDTO>> getAllGestionnaireBanques(
        GestionnaireBanqueCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GestionnaireBanques by criteria: {}", criteria);
        Page<GestionnaireBanqueDTO> page = gestionnaireBanqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gestionnaire-banques/count} : count all the gestionnaireBanques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gestionnaire-banques/count")
    public ResponseEntity<Long> countGestionnaireBanques(GestionnaireBanqueCriteria criteria) {
        log.debug("REST request to count GestionnaireBanques by criteria: {}", criteria);
        return ResponseEntity.ok().body(gestionnaireBanqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gestionnaire-banques/:id} : get the "id" gestionnaireBanque.
     *
     * @param id the id of the gestionnaireBanqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gestionnaireBanqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gestionnaire-banques/{id}")
    public ResponseEntity<GestionnaireBanqueDTO> getGestionnaireBanque(@PathVariable Long id) {
        log.debug("REST request to get GestionnaireBanque : {}", id);
        Optional<GestionnaireBanqueDTO> gestionnaireBanqueDTO = gestionnaireBanqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gestionnaireBanqueDTO);
    }

    /**
     * {@code DELETE  /gestionnaire-banques/:id} : delete the "id" gestionnaireBanque.
     *
     * @param id the id of the gestionnaireBanqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gestionnaire-banques/{id}")
    public ResponseEntity<Void> deleteGestionnaireBanque(@PathVariable Long id) {
        log.debug("REST request to delete GestionnaireBanque : {}", id);
        gestionnaireBanqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
