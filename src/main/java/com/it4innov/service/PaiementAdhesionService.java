package com.it4innov.service;

import com.it4innov.service.dto.PaiementAdhesionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.PaiementAdhesion}.
 */
public interface PaiementAdhesionService {
    /**
     * Save a paiementAdhesion.
     *
     * @param paiementAdhesionDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementAdhesionDTO save(PaiementAdhesionDTO paiementAdhesionDTO);

    /**
     * Updates a paiementAdhesion.
     *
     * @param paiementAdhesionDTO the entity to update.
     * @return the persisted entity.
     */
    PaiementAdhesionDTO update(PaiementAdhesionDTO paiementAdhesionDTO);

    /**
     * Partially updates a paiementAdhesion.
     *
     * @param paiementAdhesionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaiementAdhesionDTO> partialUpdate(PaiementAdhesionDTO paiementAdhesionDTO);

    /**
     * Get all the paiementAdhesions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaiementAdhesionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paiementAdhesion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementAdhesionDTO> findOne(Long id);

    /**
     * Delete the "id" paiementAdhesion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
