package com.it4innov.service;

import com.it4innov.service.dto.MonnaieDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Monnaie}.
 */
public interface MonnaieService {
    /**
     * Save a monnaie.
     *
     * @param monnaieDTO the entity to save.
     * @return the persisted entity.
     */
    MonnaieDTO save(MonnaieDTO monnaieDTO);

    /**
     * Updates a monnaie.
     *
     * @param monnaieDTO the entity to update.
     * @return the persisted entity.
     */
    MonnaieDTO update(MonnaieDTO monnaieDTO);

    /**
     * Partially updates a monnaie.
     *
     * @param monnaieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MonnaieDTO> partialUpdate(MonnaieDTO monnaieDTO);

    /**
     * Get all the monnaies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MonnaieDTO> findAll(Pageable pageable);

    /**
     * Get the "id" monnaie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MonnaieDTO> findOne(Long id);

    /**
     * Delete the "id" monnaie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
