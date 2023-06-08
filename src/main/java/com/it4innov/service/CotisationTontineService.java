package com.it4innov.service;

import com.it4innov.service.dto.CotisationTontineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.CotisationTontine}.
 */
public interface CotisationTontineService {
    /**
     * Save a cotisationTontine.
     *
     * @param cotisationTontineDTO the entity to save.
     * @return the persisted entity.
     */
    CotisationTontineDTO save(CotisationTontineDTO cotisationTontineDTO);

    /**
     * Updates a cotisationTontine.
     *
     * @param cotisationTontineDTO the entity to update.
     * @return the persisted entity.
     */
    CotisationTontineDTO update(CotisationTontineDTO cotisationTontineDTO);

    /**
     * Partially updates a cotisationTontine.
     *
     * @param cotisationTontineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CotisationTontineDTO> partialUpdate(CotisationTontineDTO cotisationTontineDTO);

    /**
     * Get all the cotisationTontines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CotisationTontineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cotisationTontine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CotisationTontineDTO> findOne(Long id);

    /**
     * Delete the "id" cotisationTontine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
