package com.it4innov.service;

import com.it4innov.service.dto.PaiementTontineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.PaiementTontine}.
 */
public interface PaiementTontineService {
    /**
     * Save a paiementTontine.
     *
     * @param paiementTontineDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementTontineDTO save(PaiementTontineDTO paiementTontineDTO);

    /**
     * Updates a paiementTontine.
     *
     * @param paiementTontineDTO the entity to update.
     * @return the persisted entity.
     */
    PaiementTontineDTO update(PaiementTontineDTO paiementTontineDTO);

    /**
     * Partially updates a paiementTontine.
     *
     * @param paiementTontineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaiementTontineDTO> partialUpdate(PaiementTontineDTO paiementTontineDTO);

    /**
     * Get all the paiementTontines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaiementTontineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paiementTontine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementTontineDTO> findOne(Long id);

    /**
     * Delete the "id" paiementTontine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
