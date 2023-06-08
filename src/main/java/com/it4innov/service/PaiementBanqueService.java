package com.it4innov.service;

import com.it4innov.service.dto.PaiementBanqueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.PaiementBanque}.
 */
public interface PaiementBanqueService {
    /**
     * Save a paiementBanque.
     *
     * @param paiementBanqueDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementBanqueDTO save(PaiementBanqueDTO paiementBanqueDTO);

    /**
     * Updates a paiementBanque.
     *
     * @param paiementBanqueDTO the entity to update.
     * @return the persisted entity.
     */
    PaiementBanqueDTO update(PaiementBanqueDTO paiementBanqueDTO);

    /**
     * Partially updates a paiementBanque.
     *
     * @param paiementBanqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaiementBanqueDTO> partialUpdate(PaiementBanqueDTO paiementBanqueDTO);

    /**
     * Get all the paiementBanques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaiementBanqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paiementBanque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementBanqueDTO> findOne(Long id);

    /**
     * Delete the "id" paiementBanque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
