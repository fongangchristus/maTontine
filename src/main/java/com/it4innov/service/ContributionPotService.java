package com.it4innov.service;

import com.it4innov.service.dto.ContributionPotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.ContributionPot}.
 */
public interface ContributionPotService {
    /**
     * Save a contributionPot.
     *
     * @param contributionPotDTO the entity to save.
     * @return the persisted entity.
     */
    ContributionPotDTO save(ContributionPotDTO contributionPotDTO);

    /**
     * Updates a contributionPot.
     *
     * @param contributionPotDTO the entity to update.
     * @return the persisted entity.
     */
    ContributionPotDTO update(ContributionPotDTO contributionPotDTO);

    /**
     * Partially updates a contributionPot.
     *
     * @param contributionPotDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContributionPotDTO> partialUpdate(ContributionPotDTO contributionPotDTO);

    /**
     * Get all the contributionPots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContributionPotDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contributionPot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContributionPotDTO> findOne(Long id);

    /**
     * Delete the "id" contributionPot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
