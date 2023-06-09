package com.it4innov.service;

import com.it4innov.service.dto.ExerciseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Exercise}.
 */
public interface ExerciseService {
    /**
     * Save a exercise.
     *
     * @param exerciseDTO the entity to save.
     * @return the persisted entity.
     */
    ExerciseDTO save(ExerciseDTO exerciseDTO);

    /**
     * Updates a exercise.
     *
     * @param exerciseDTO the entity to update.
     * @return the persisted entity.
     */
    ExerciseDTO update(ExerciseDTO exerciseDTO);

    /**
     * Partially updates a exercise.
     *
     * @param exerciseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseDTO> partialUpdate(ExerciseDTO exerciseDTO);

    /**
     * Get all the exercises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseDTO> findOne(Long id);

    /**
     * Delete the "id" exercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
