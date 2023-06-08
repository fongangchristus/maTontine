package com.it4innov.service;

import com.it4innov.service.dto.AssembleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Assemble}.
 */
public interface AssembleService {
    /**
     * Save a assemble.
     *
     * @param assembleDTO the entity to save.
     * @return the persisted entity.
     */
    AssembleDTO save(AssembleDTO assembleDTO);

    /**
     * Updates a assemble.
     *
     * @param assembleDTO the entity to update.
     * @return the persisted entity.
     */
    AssembleDTO update(AssembleDTO assembleDTO);

    /**
     * Partially updates a assemble.
     *
     * @param assembleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssembleDTO> partialUpdate(AssembleDTO assembleDTO);

    /**
     * Get all the assembles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssembleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assemble.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssembleDTO> findOne(Long id);

    /**
     * Delete the "id" assemble.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
