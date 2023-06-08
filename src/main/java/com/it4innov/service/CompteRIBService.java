package com.it4innov.service;

import com.it4innov.service.dto.CompteRIBDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.CompteRIB}.
 */
public interface CompteRIBService {
    /**
     * Save a compteRIB.
     *
     * @param compteRIBDTO the entity to save.
     * @return the persisted entity.
     */
    CompteRIBDTO save(CompteRIBDTO compteRIBDTO);

    /**
     * Updates a compteRIB.
     *
     * @param compteRIBDTO the entity to update.
     * @return the persisted entity.
     */
    CompteRIBDTO update(CompteRIBDTO compteRIBDTO);

    /**
     * Partially updates a compteRIB.
     *
     * @param compteRIBDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompteRIBDTO> partialUpdate(CompteRIBDTO compteRIBDTO);

    /**
     * Get all the compteRIBS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompteRIBDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compteRIB.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompteRIBDTO> findOne(Long id);

    /**
     * Delete the "id" compteRIB.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
