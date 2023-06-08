package com.it4innov.service;

import com.it4innov.service.dto.SessionTontineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.SessionTontine}.
 */
public interface SessionTontineService {
    /**
     * Save a sessionTontine.
     *
     * @param sessionTontineDTO the entity to save.
     * @return the persisted entity.
     */
    SessionTontineDTO save(SessionTontineDTO sessionTontineDTO);

    /**
     * Updates a sessionTontine.
     *
     * @param sessionTontineDTO the entity to update.
     * @return the persisted entity.
     */
    SessionTontineDTO update(SessionTontineDTO sessionTontineDTO);

    /**
     * Partially updates a sessionTontine.
     *
     * @param sessionTontineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SessionTontineDTO> partialUpdate(SessionTontineDTO sessionTontineDTO);

    /**
     * Get all the sessionTontines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SessionTontineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sessionTontine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionTontineDTO> findOne(Long id);

    /**
     * Delete the "id" sessionTontine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
