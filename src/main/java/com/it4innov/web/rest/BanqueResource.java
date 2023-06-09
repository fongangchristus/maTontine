package com.it4innov.web.rest;

import com.it4innov.repository.BanqueRepository;
import com.it4innov.service.BanqueQueryService;
import com.it4innov.service.BanqueService;
import com.it4innov.service.criteria.BanqueCriteria;
import com.it4innov.service.dto.BanqueDTO;
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
 * REST controller for managing {@link com.it4innov.domain.Banque}.
 */
@RestController
@RequestMapping("/api")
public class BanqueResource {

    private final Logger log = LoggerFactory.getLogger(BanqueResource.class);

    private static final String ENTITY_NAME = "banque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BanqueService banqueService;

    private final BanqueRepository banqueRepository;

    private final BanqueQueryService banqueQueryService;

    public BanqueResource(BanqueService banqueService, BanqueRepository banqueRepository, BanqueQueryService banqueQueryService) {
        this.banqueService = banqueService;
        this.banqueRepository = banqueRepository;
        this.banqueQueryService = banqueQueryService;
    }

    /**
     * {@code POST  /banques} : Create a new banque.
     *
     * @param banqueDTO the banqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new banqueDTO, or with status {@code 400 (Bad Request)} if the banque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banques")
    public ResponseEntity<BanqueDTO> createBanque(@Valid @RequestBody BanqueDTO banqueDTO) throws URISyntaxException {
        log.debug("REST request to save Banque : {}", banqueDTO);
        if (banqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new banque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BanqueDTO result = banqueService.save(banqueDTO);
        return ResponseEntity
            .created(new URI("/api/banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /banques/:id} : Updates an existing banque.
     *
     * @param id the id of the banqueDTO to save.
     * @param banqueDTO the banqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated banqueDTO,
     * or with status {@code 400 (Bad Request)} if the banqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the banqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/banques/{id}")
    public ResponseEntity<BanqueDTO> updateBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BanqueDTO banqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Banque : {}, {}", id, banqueDTO);
        if (banqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, banqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!banqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BanqueDTO result = banqueService.update(banqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, banqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /banques/:id} : Partial updates given fields of an existing banque, field will ignore if it is null
     *
     * @param id the id of the banqueDTO to save.
     * @param banqueDTO the banqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated banqueDTO,
     * or with status {@code 400 (Bad Request)} if the banqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the banqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the banqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/banques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BanqueDTO> partialUpdateBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BanqueDTO banqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Banque partially : {}, {}", id, banqueDTO);
        if (banqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, banqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!banqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BanqueDTO> result = banqueService.partialUpdate(banqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, banqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /banques} : get all the banques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banques in body.
     */
    @GetMapping("/banques")
    public ResponseEntity<List<BanqueDTO>> getAllBanques(
        BanqueCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Banques by criteria: {}", criteria);
        Page<BanqueDTO> page = banqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /banques/count} : count all the banques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/banques/count")
    public ResponseEntity<Long> countBanques(BanqueCriteria criteria) {
        log.debug("REST request to count Banques by criteria: {}", criteria);
        return ResponseEntity.ok().body(banqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /banques/:id} : get the "id" banque.
     *
     * @param id the id of the banqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the banqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/banques/{id}")
    public ResponseEntity<BanqueDTO> getBanque(@PathVariable Long id) {
        log.debug("REST request to get Banque : {}", id);
        Optional<BanqueDTO> banqueDTO = banqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(banqueDTO);
    }

    /**
     * {@code DELETE  /banques/:id} : delete the "id" banque.
     *
     * @param id the id of the banqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/banques/{id}")
    public ResponseEntity<Void> deleteBanque(@PathVariable Long id) {
        log.debug("REST request to delete Banque : {}", id);
        banqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
