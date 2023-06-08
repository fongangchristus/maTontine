package com.it4innov.service;

import com.it4innov.service.dto.CotisationBanqueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.CotisationBanque}.
 */
public interface CotisationBanqueService {
    /**
     * Save a cotisationBanque.
     *
     * @param cotisationBanqueDTO the entity to save.
     * @return the persisted entity.
     */
    CotisationBanqueDTO save(CotisationBanqueDTO cotisationBanqueDTO);

    /**
     * Updates a cotisationBanque.
     *
     * @param cotisationBanqueDTO the entity to update.
     * @return the persisted entity.
     */
    CotisationBanqueDTO update(CotisationBanqueDTO cotisationBanqueDTO);

    /**
     * Partially updates a cotisationBanque.
     *
     * @param cotisationBanqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CotisationBanqueDTO> partialUpdate(CotisationBanqueDTO cotisationBanqueDTO);

    /**
     * Get all the cotisationBanques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CotisationBanqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cotisationBanque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CotisationBanqueDTO> findOne(Long id);

    /**
     * Delete the "id" cotisationBanque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
