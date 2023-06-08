package com.it4innov.service;

import com.it4innov.service.dto.AssociationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Association}.
 */
public interface AssociationService {
    /**
     * Save a association.
     *
     * @param associationDTO the entity to save.
     * @return the persisted entity.
     */
    AssociationDTO save(AssociationDTO associationDTO);

    /**
     * Updates a association.
     *
     * @param associationDTO the entity to update.
     * @return the persisted entity.
     */
    AssociationDTO update(AssociationDTO associationDTO);

    /**
     * Partially updates a association.
     *
     * @param associationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssociationDTO> partialUpdate(AssociationDTO associationDTO);

    /**
     * Get all the associations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssociationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" association.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssociationDTO> findOne(Long id);

    /**
     * Delete the "id" association.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
