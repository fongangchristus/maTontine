package com.it4innov.service;

import com.it4innov.service.dto.GestionnaireBanqueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.GestionnaireBanque}.
 */
public interface GestionnaireBanqueService {
    /**
     * Save a gestionnaireBanque.
     *
     * @param gestionnaireBanqueDTO the entity to save.
     * @return the persisted entity.
     */
    GestionnaireBanqueDTO save(GestionnaireBanqueDTO gestionnaireBanqueDTO);

    /**
     * Updates a gestionnaireBanque.
     *
     * @param gestionnaireBanqueDTO the entity to update.
     * @return the persisted entity.
     */
    GestionnaireBanqueDTO update(GestionnaireBanqueDTO gestionnaireBanqueDTO);

    /**
     * Partially updates a gestionnaireBanque.
     *
     * @param gestionnaireBanqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GestionnaireBanqueDTO> partialUpdate(GestionnaireBanqueDTO gestionnaireBanqueDTO);

    /**
     * Get all the gestionnaireBanques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GestionnaireBanqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" gestionnaireBanque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GestionnaireBanqueDTO> findOne(Long id);

    /**
     * Delete the "id" gestionnaireBanque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
