package com.it4innov.service;

import com.it4innov.service.dto.PaiementSanctionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.PaiementSanction}.
 */
public interface PaiementSanctionService {
    /**
     * Save a paiementSanction.
     *
     * @param paiementSanctionDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementSanctionDTO save(PaiementSanctionDTO paiementSanctionDTO);

    /**
     * Updates a paiementSanction.
     *
     * @param paiementSanctionDTO the entity to update.
     * @return the persisted entity.
     */
    PaiementSanctionDTO update(PaiementSanctionDTO paiementSanctionDTO);

    /**
     * Partially updates a paiementSanction.
     *
     * @param paiementSanctionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaiementSanctionDTO> partialUpdate(PaiementSanctionDTO paiementSanctionDTO);

    /**
     * Get all the paiementSanctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaiementSanctionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paiementSanction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementSanctionDTO> findOne(Long id);

    /**
     * Delete the "id" paiementSanction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
