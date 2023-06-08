package com.it4innov.web.rest;

import com.it4innov.repository.CommentairePotRepository;
import com.it4innov.service.CommentairePotQueryService;
import com.it4innov.service.CommentairePotService;
import com.it4innov.service.criteria.CommentairePotCriteria;
import com.it4innov.service.dto.CommentairePotDTO;
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
 * REST controller for managing {@link com.it4innov.domain.CommentairePot}.
 */
@RestController
@RequestMapping("/api")
public class CommentairePotResource {

    private final Logger log = LoggerFactory.getLogger(CommentairePotResource.class);

    private static final String ENTITY_NAME = "commentairePot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentairePotService commentairePotService;

    private final CommentairePotRepository commentairePotRepository;

    private final CommentairePotQueryService commentairePotQueryService;

    public CommentairePotResource(
        CommentairePotService commentairePotService,
        CommentairePotRepository commentairePotRepository,
        CommentairePotQueryService commentairePotQueryService
    ) {
        this.commentairePotService = commentairePotService;
        this.commentairePotRepository = commentairePotRepository;
        this.commentairePotQueryService = commentairePotQueryService;
    }

    /**
     * {@code POST  /commentaire-pots} : Create a new commentairePot.
     *
     * @param commentairePotDTO the commentairePotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentairePotDTO, or with status {@code 400 (Bad Request)} if the commentairePot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commentaire-pots")
    public ResponseEntity<CommentairePotDTO> createCommentairePot(@Valid @RequestBody CommentairePotDTO commentairePotDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommentairePot : {}", commentairePotDTO);
        if (commentairePotDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentairePot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentairePotDTO result = commentairePotService.save(commentairePotDTO);
        return ResponseEntity
            .created(new URI("/api/commentaire-pots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commentaire-pots/:id} : Updates an existing commentairePot.
     *
     * @param id the id of the commentairePotDTO to save.
     * @param commentairePotDTO the commentairePotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentairePotDTO,
     * or with status {@code 400 (Bad Request)} if the commentairePotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentairePotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commentaire-pots/{id}")
    public ResponseEntity<CommentairePotDTO> updateCommentairePot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommentairePotDTO commentairePotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommentairePot : {}, {}", id, commentairePotDTO);
        if (commentairePotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentairePotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentairePotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommentairePotDTO result = commentairePotService.update(commentairePotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentairePotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commentaire-pots/:id} : Partial updates given fields of an existing commentairePot, field will ignore if it is null
     *
     * @param id the id of the commentairePotDTO to save.
     * @param commentairePotDTO the commentairePotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentairePotDTO,
     * or with status {@code 400 (Bad Request)} if the commentairePotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentairePotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentairePotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commentaire-pots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommentairePotDTO> partialUpdateCommentairePot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommentairePotDTO commentairePotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommentairePot partially : {}, {}", id, commentairePotDTO);
        if (commentairePotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentairePotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentairePotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentairePotDTO> result = commentairePotService.partialUpdate(commentairePotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentairePotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /commentaire-pots} : get all the commentairePots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentairePots in body.
     */
    @GetMapping("/commentaire-pots")
    public ResponseEntity<List<CommentairePotDTO>> getAllCommentairePots(
        CommentairePotCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CommentairePots by criteria: {}", criteria);
        Page<CommentairePotDTO> page = commentairePotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commentaire-pots/count} : count all the commentairePots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/commentaire-pots/count")
    public ResponseEntity<Long> countCommentairePots(CommentairePotCriteria criteria) {
        log.debug("REST request to count CommentairePots by criteria: {}", criteria);
        return ResponseEntity.ok().body(commentairePotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /commentaire-pots/:id} : get the "id" commentairePot.
     *
     * @param id the id of the commentairePotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentairePotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commentaire-pots/{id}")
    public ResponseEntity<CommentairePotDTO> getCommentairePot(@PathVariable Long id) {
        log.debug("REST request to get CommentairePot : {}", id);
        Optional<CommentairePotDTO> commentairePotDTO = commentairePotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentairePotDTO);
    }

    /**
     * {@code DELETE  /commentaire-pots/:id} : delete the "id" commentairePot.
     *
     * @param id the id of the commentairePotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commentaire-pots/{id}")
    public ResponseEntity<Void> deleteCommentairePot(@PathVariable Long id) {
        log.debug("REST request to delete CommentairePot : {}", id);
        commentairePotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
