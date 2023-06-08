package com.it4innov.service;

import com.it4innov.service.dto.SanctionConfigurationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.SanctionConfiguration}.
 */
public interface SanctionConfigurationService {
    /**
     * Save a sanctionConfiguration.
     *
     * @param sanctionConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    SanctionConfigurationDTO save(SanctionConfigurationDTO sanctionConfigurationDTO);

    /**
     * Updates a sanctionConfiguration.
     *
     * @param sanctionConfigurationDTO the entity to update.
     * @return the persisted entity.
     */
    SanctionConfigurationDTO update(SanctionConfigurationDTO sanctionConfigurationDTO);

    /**
     * Partially updates a sanctionConfiguration.
     *
     * @param sanctionConfigurationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SanctionConfigurationDTO> partialUpdate(SanctionConfigurationDTO sanctionConfigurationDTO);

    /**
     * Get all the sanctionConfigurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SanctionConfigurationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sanctionConfiguration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SanctionConfigurationDTO> findOne(Long id);

    /**
     * Delete the "id" sanctionConfiguration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
