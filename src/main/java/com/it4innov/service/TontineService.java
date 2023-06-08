package com.it4innov.service;

import com.it4innov.service.dto.TontineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Tontine}.
 */
public interface TontineService {
    /**
     * Save a tontine.
     *
     * @param tontineDTO the entity to save.
     * @return the persisted entity.
     */
    TontineDTO save(TontineDTO tontineDTO);

    /**
     * Updates a tontine.
     *
     * @param tontineDTO the entity to update.
     * @return the persisted entity.
     */
    TontineDTO update(TontineDTO tontineDTO);

    /**
     * Partially updates a tontine.
     *
     * @param tontineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TontineDTO> partialUpdate(TontineDTO tontineDTO);

    /**
     * Get all the tontines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TontineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tontine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TontineDTO> findOne(Long id);

    /**
     * Delete the "id" tontine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
