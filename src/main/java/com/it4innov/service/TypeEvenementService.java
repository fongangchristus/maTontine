package com.it4innov.service;

import com.it4innov.service.dto.TypeEvenementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.TypeEvenement}.
 */
public interface TypeEvenementService {
    /**
     * Save a typeEvenement.
     *
     * @param typeEvenementDTO the entity to save.
     * @return the persisted entity.
     */
    TypeEvenementDTO save(TypeEvenementDTO typeEvenementDTO);

    /**
     * Updates a typeEvenement.
     *
     * @param typeEvenementDTO the entity to update.
     * @return the persisted entity.
     */
    TypeEvenementDTO update(TypeEvenementDTO typeEvenementDTO);

    /**
     * Partially updates a typeEvenement.
     *
     * @param typeEvenementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeEvenementDTO> partialUpdate(TypeEvenementDTO typeEvenementDTO);

    /**
     * Get all the typeEvenements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeEvenementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeEvenement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeEvenementDTO> findOne(Long id);

    /**
     * Delete the "id" typeEvenement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
