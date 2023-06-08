package com.it4innov.service;

import com.it4innov.service.dto.CompteBanqueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.CompteBanque}.
 */
public interface CompteBanqueService {
    /**
     * Save a compteBanque.
     *
     * @param compteBanqueDTO the entity to save.
     * @return the persisted entity.
     */
    CompteBanqueDTO save(CompteBanqueDTO compteBanqueDTO);

    /**
     * Updates a compteBanque.
     *
     * @param compteBanqueDTO the entity to update.
     * @return the persisted entity.
     */
    CompteBanqueDTO update(CompteBanqueDTO compteBanqueDTO);

    /**
     * Partially updates a compteBanque.
     *
     * @param compteBanqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompteBanqueDTO> partialUpdate(CompteBanqueDTO compteBanqueDTO);

    /**
     * Get all the compteBanques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompteBanqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compteBanque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompteBanqueDTO> findOne(Long id);

    /**
     * Delete the "id" compteBanque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
