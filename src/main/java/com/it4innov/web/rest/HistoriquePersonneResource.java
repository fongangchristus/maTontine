package com.it4innov.web.rest;

import com.it4innov.repository.HistoriquePersonneRepository;
import com.it4innov.service.HistoriquePersonneQueryService;
import com.it4innov.service.HistoriquePersonneService;
import com.it4innov.service.criteria.HistoriquePersonneCriteria;
import com.it4innov.service.dto.HistoriquePersonneDTO;
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
 * REST controller for managing {@link com.it4innov.domain.HistoriquePersonne}.
 */
@RestController
@RequestMapping("/api")
public class HistoriquePersonneResource {

    private final Logger log = LoggerFactory.getLogger(HistoriquePersonneResource.class);

    private static final String ENTITY_NAME = "historiquePersonne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriquePersonneService historiquePersonneService;

    private final HistoriquePersonneRepository historiquePersonneRepository;

    private final HistoriquePersonneQueryService historiquePersonneQueryService;

    public HistoriquePersonneResource(
        HistoriquePersonneService historiquePersonneService,
        HistoriquePersonneRepository historiquePersonneRepository,
        HistoriquePersonneQueryService historiquePersonneQueryService
    ) {
        this.historiquePersonneService = historiquePersonneService;
        this.historiquePersonneRepository = historiquePersonneRepository;
        this.historiquePersonneQueryService = historiquePersonneQueryService;
    }

    /**
     * {@code POST  /historique-personnes} : Create a new historiquePersonne.
     *
     * @param historiquePersonneDTO the historiquePersonneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiquePersonneDTO, or with status {@code 400 (Bad Request)} if the historiquePersonne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historique-personnes")
    public ResponseEntity<HistoriquePersonneDTO> createHistoriquePersonne(@Valid @RequestBody HistoriquePersonneDTO historiquePersonneDTO)
        throws URISyntaxException {
        log.debug("REST request to save HistoriquePersonne : {}", historiquePersonneDTO);
        if (historiquePersonneDTO.getId() != null) {
            throw new BadRequestAlertException("A new historiquePersonne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoriquePersonneDTO result = historiquePersonneService.save(historiquePersonneDTO);
        return ResponseEntity
            .created(new URI("/api/historique-personnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historique-personnes/:id} : Updates an existing historiquePersonne.
     *
     * @param id the id of the historiquePersonneDTO to save.
     * @param historiquePersonneDTO the historiquePersonneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiquePersonneDTO,
     * or with status {@code 400 (Bad Request)} if the historiquePersonneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiquePersonneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historique-personnes/{id}")
    public ResponseEntity<HistoriquePersonneDTO> updateHistoriquePersonne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriquePersonneDTO historiquePersonneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HistoriquePersonne : {}, {}", id, historiquePersonneDTO);
        if (historiquePersonneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiquePersonneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiquePersonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoriquePersonneDTO result = historiquePersonneService.update(historiquePersonneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiquePersonneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /historique-personnes/:id} : Partial updates given fields of an existing historiquePersonne, field will ignore if it is null
     *
     * @param id the id of the historiquePersonneDTO to save.
     * @param historiquePersonneDTO the historiquePersonneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiquePersonneDTO,
     * or with status {@code 400 (Bad Request)} if the historiquePersonneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historiquePersonneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiquePersonneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historique-personnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoriquePersonneDTO> partialUpdateHistoriquePersonne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriquePersonneDTO historiquePersonneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoriquePersonne partially : {}, {}", id, historiquePersonneDTO);
        if (historiquePersonneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiquePersonneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiquePersonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoriquePersonneDTO> result = historiquePersonneService.partialUpdate(historiquePersonneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiquePersonneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historique-personnes} : get all the historiquePersonnes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiquePersonnes in body.
     */
    @GetMapping("/historique-personnes")
    public ResponseEntity<List<HistoriquePersonneDTO>> getAllHistoriquePersonnes(
        HistoriquePersonneCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HistoriquePersonnes by criteria: {}", criteria);
        Page<HistoriquePersonneDTO> page = historiquePersonneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-personnes/count} : count all the historiquePersonnes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/historique-personnes/count")
    public ResponseEntity<Long> countHistoriquePersonnes(HistoriquePersonneCriteria criteria) {
        log.debug("REST request to count HistoriquePersonnes by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiquePersonneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historique-personnes/:id} : get the "id" historiquePersonne.
     *
     * @param id the id of the historiquePersonneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiquePersonneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historique-personnes/{id}")
    public ResponseEntity<HistoriquePersonneDTO> getHistoriquePersonne(@PathVariable Long id) {
        log.debug("REST request to get HistoriquePersonne : {}", id);
        Optional<HistoriquePersonneDTO> historiquePersonneDTO = historiquePersonneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiquePersonneDTO);
    }

    /**
     * {@code DELETE  /historique-personnes/:id} : delete the "id" historiquePersonne.
     *
     * @param id the id of the historiquePersonneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historique-personnes/{id}")
    public ResponseEntity<Void> deleteHistoriquePersonne(@PathVariable Long id) {
        log.debug("REST request to delete HistoriquePersonne : {}", id);
        historiquePersonneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
