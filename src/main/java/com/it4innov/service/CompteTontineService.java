package com.it4innov.service;

import com.it4innov.service.dto.CompteTontineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.CompteTontine}.
 */
public interface CompteTontineService {
    /**
     * Save a compteTontine.
     *
     * @param compteTontineDTO the entity to save.
     * @return the persisted entity.
     */
    CompteTontineDTO save(CompteTontineDTO compteTontineDTO);

    /**
     * Updates a compteTontine.
     *
     * @param compteTontineDTO the entity to update.
     * @return the persisted entity.
     */
    CompteTontineDTO update(CompteTontineDTO compteTontineDTO);

    /**
     * Partially updates a compteTontine.
     *
     * @param compteTontineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompteTontineDTO> partialUpdate(CompteTontineDTO compteTontineDTO);

    /**
     * Get all the compteTontines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompteTontineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compteTontine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompteTontineDTO> findOne(Long id);

    /**
     * Delete the "id" compteTontine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
