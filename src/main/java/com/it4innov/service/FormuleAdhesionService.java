package com.it4innov.service;

import com.it4innov.service.dto.FormuleAdhesionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.FormuleAdhesion}.
 */
public interface FormuleAdhesionService {
    /**
     * Save a formuleAdhesion.
     *
     * @param formuleAdhesionDTO the entity to save.
     * @return the persisted entity.
     */
    FormuleAdhesionDTO save(FormuleAdhesionDTO formuleAdhesionDTO);

    /**
     * Updates a formuleAdhesion.
     *
     * @param formuleAdhesionDTO the entity to update.
     * @return the persisted entity.
     */
    FormuleAdhesionDTO update(FormuleAdhesionDTO formuleAdhesionDTO);

    /**
     * Partially updates a formuleAdhesion.
     *
     * @param formuleAdhesionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormuleAdhesionDTO> partialUpdate(FormuleAdhesionDTO formuleAdhesionDTO);

    /**
     * Get all the formuleAdhesions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormuleAdhesionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formuleAdhesion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormuleAdhesionDTO> findOne(Long id);

    /**
     * Delete the "id" formuleAdhesion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
