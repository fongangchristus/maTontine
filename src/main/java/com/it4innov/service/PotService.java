package com.it4innov.service;

import com.it4innov.service.dto.PotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.Pot}.
 */
public interface PotService {
    /**
     * Save a pot.
     *
     * @param potDTO the entity to save.
     * @return the persisted entity.
     */
    PotDTO save(PotDTO potDTO);

    /**
     * Updates a pot.
     *
     * @param potDTO the entity to update.
     * @return the persisted entity.
     */
    PotDTO update(PotDTO potDTO);

    /**
     * Partially updates a pot.
     *
     * @param potDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PotDTO> partialUpdate(PotDTO potDTO);

    /**
     * Get all the pots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PotDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PotDTO> findOne(Long id);

    /**
     * Delete the "id" pot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
