package com.it4innov.service;

import com.it4innov.service.dto.HistoriquePersonneDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.HistoriquePersonne}.
 */
public interface HistoriquePersonneService {
    /**
     * Save a historiquePersonne.
     *
     * @param historiquePersonneDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriquePersonneDTO save(HistoriquePersonneDTO historiquePersonneDTO);

    /**
     * Updates a historiquePersonne.
     *
     * @param historiquePersonneDTO the entity to update.
     * @return the persisted entity.
     */
    HistoriquePersonneDTO update(HistoriquePersonneDTO historiquePersonneDTO);

    /**
     * Partially updates a historiquePersonne.
     *
     * @param historiquePersonneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoriquePersonneDTO> partialUpdate(HistoriquePersonneDTO historiquePersonneDTO);

    /**
     * Get all the historiquePersonnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriquePersonneDTO> findAll(Pageable pageable);

    /**
     * Get the "id" historiquePersonne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriquePersonneDTO> findOne(Long id);

    /**
     * Delete the "id" historiquePersonne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
