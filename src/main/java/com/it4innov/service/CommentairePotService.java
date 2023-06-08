package com.it4innov.service;

import com.it4innov.service.dto.CommentairePotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.CommentairePot}.
 */
public interface CommentairePotService {
    /**
     * Save a commentairePot.
     *
     * @param commentairePotDTO the entity to save.
     * @return the persisted entity.
     */
    CommentairePotDTO save(CommentairePotDTO commentairePotDTO);

    /**
     * Updates a commentairePot.
     *
     * @param commentairePotDTO the entity to update.
     * @return the persisted entity.
     */
    CommentairePotDTO update(CommentairePotDTO commentairePotDTO);

    /**
     * Partially updates a commentairePot.
     *
     * @param commentairePotDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentairePotDTO> partialUpdate(CommentairePotDTO commentairePotDTO);

    /**
     * Get all the commentairePots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentairePotDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentairePot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentairePotDTO> findOne(Long id);

    /**
     * Delete the "id" commentairePot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
