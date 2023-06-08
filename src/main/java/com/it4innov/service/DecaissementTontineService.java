package com.it4innov.service;

import com.it4innov.service.dto.DecaissementTontineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.DecaissementTontine}.
 */
public interface DecaissementTontineService {
    /**
     * Save a decaissementTontine.
     *
     * @param decaissementTontineDTO the entity to save.
     * @return the persisted entity.
     */
    DecaissementTontineDTO save(DecaissementTontineDTO decaissementTontineDTO);

    /**
     * Updates a decaissementTontine.
     *
     * @param decaissementTontineDTO the entity to update.
     * @return the persisted entity.
     */
    DecaissementTontineDTO update(DecaissementTontineDTO decaissementTontineDTO);

    /**
     * Partially updates a decaissementTontine.
     *
     * @param decaissementTontineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DecaissementTontineDTO> partialUpdate(DecaissementTontineDTO decaissementTontineDTO);

    /**
     * Get all the decaissementTontines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DecaissementTontineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" decaissementTontine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DecaissementTontineDTO> findOne(Long id);

    /**
     * Delete the "id" decaissementTontine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
