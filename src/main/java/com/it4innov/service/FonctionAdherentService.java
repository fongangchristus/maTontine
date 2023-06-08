package com.it4innov.service;

import com.it4innov.service.dto.FonctionAdherentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.FonctionAdherent}.
 */
public interface FonctionAdherentService {
    /**
     * Save a fonctionAdherent.
     *
     * @param fonctionAdherentDTO the entity to save.
     * @return the persisted entity.
     */
    FonctionAdherentDTO save(FonctionAdherentDTO fonctionAdherentDTO);

    /**
     * Updates a fonctionAdherent.
     *
     * @param fonctionAdherentDTO the entity to update.
     * @return the persisted entity.
     */
    FonctionAdherentDTO update(FonctionAdherentDTO fonctionAdherentDTO);

    /**
     * Partially updates a fonctionAdherent.
     *
     * @param fonctionAdherentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FonctionAdherentDTO> partialUpdate(FonctionAdherentDTO fonctionAdherentDTO);

    /**
     * Get all the fonctionAdherents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FonctionAdherentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fonctionAdherent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FonctionAdherentDTO> findOne(Long id);

    /**
     * Delete the "id" fonctionAdherent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
