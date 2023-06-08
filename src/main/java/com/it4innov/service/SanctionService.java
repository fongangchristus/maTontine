package com.it4innov.service;

import com.it4innov.service.dto.SanctionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Sanction}.
 */
public interface SanctionService {
    /**
     * Save a sanction.
     *
     * @param sanctionDTO the entity to save.
     * @return the persisted entity.
     */
    SanctionDTO save(SanctionDTO sanctionDTO);

    /**
     * Updates a sanction.
     *
     * @param sanctionDTO the entity to update.
     * @return the persisted entity.
     */
    SanctionDTO update(SanctionDTO sanctionDTO);

    /**
     * Partially updates a sanction.
     *
     * @param sanctionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SanctionDTO> partialUpdate(SanctionDTO sanctionDTO);

    /**
     * Get all the sanctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SanctionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sanction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SanctionDTO> findOne(Long id);

    /**
     * Delete the "id" sanction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
