package com.it4innov.service;

import com.it4innov.service.dto.TypePotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.TypePot}.
 */
public interface TypePotService {
    /**
     * Save a typePot.
     *
     * @param typePotDTO the entity to save.
     * @return the persisted entity.
     */
    TypePotDTO save(TypePotDTO typePotDTO);

    /**
     * Updates a typePot.
     *
     * @param typePotDTO the entity to update.
     * @return the persisted entity.
     */
    TypePotDTO update(TypePotDTO typePotDTO);

    /**
     * Partially updates a typePot.
     *
     * @param typePotDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypePotDTO> partialUpdate(TypePotDTO typePotDTO);

    /**
     * Get all the typePots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypePotDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typePot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypePotDTO> findOne(Long id);

    /**
     * Delete the "id" typePot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
