package com.it4innov.service;

import com.it4innov.service.dto.EvenementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Evenement}.
 */
public interface EvenementService {
    /**
     * Save a evenement.
     *
     * @param evenementDTO the entity to save.
     * @return the persisted entity.
     */
    EvenementDTO save(EvenementDTO evenementDTO);

    /**
     * Updates a evenement.
     *
     * @param evenementDTO the entity to update.
     * @return the persisted entity.
     */
    EvenementDTO update(EvenementDTO evenementDTO);

    /**
     * Partially updates a evenement.
     *
     * @param evenementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EvenementDTO> partialUpdate(EvenementDTO evenementDTO);

    /**
     * Get all the evenements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvenementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" evenement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvenementDTO> findOne(Long id);

    /**
     * Delete the "id" evenement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
