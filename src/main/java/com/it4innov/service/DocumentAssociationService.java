package com.it4innov.service;

import com.it4innov.service.dto.DocumentAssociationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.DocumentAssociation}.
 */
public interface DocumentAssociationService {
    /**
     * Save a documentAssociation.
     *
     * @param documentAssociationDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentAssociationDTO save(DocumentAssociationDTO documentAssociationDTO);

    /**
     * Updates a documentAssociation.
     *
     * @param documentAssociationDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentAssociationDTO update(DocumentAssociationDTO documentAssociationDTO);

    /**
     * Partially updates a documentAssociation.
     *
     * @param documentAssociationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentAssociationDTO> partialUpdate(DocumentAssociationDTO documentAssociationDTO);

    /**
     * Get all the documentAssociations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentAssociationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentAssociation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentAssociationDTO> findOne(Long id);

    /**
     * Delete the "id" documentAssociation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
