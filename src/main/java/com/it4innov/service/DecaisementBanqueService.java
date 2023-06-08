package com.it4innov.service;

import com.it4innov.service.dto.DecaisementBanqueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.DecaisementBanque}.
 */
public interface DecaisementBanqueService {
    /**
     * Save a decaisementBanque.
     *
     * @param decaisementBanqueDTO the entity to save.
     * @return the persisted entity.
     */
    DecaisementBanqueDTO save(DecaisementBanqueDTO decaisementBanqueDTO);

    /**
     * Updates a decaisementBanque.
     *
     * @param decaisementBanqueDTO the entity to update.
     * @return the persisted entity.
     */
    DecaisementBanqueDTO update(DecaisementBanqueDTO decaisementBanqueDTO);

    /**
     * Partially updates a decaisementBanque.
     *
     * @param decaisementBanqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DecaisementBanqueDTO> partialUpdate(DecaisementBanqueDTO decaisementBanqueDTO);

    /**
     * Get all the decaisementBanques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DecaisementBanqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" decaisementBanque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DecaisementBanqueDTO> findOne(Long id);

    /**
     * Delete the "id" decaisementBanque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
