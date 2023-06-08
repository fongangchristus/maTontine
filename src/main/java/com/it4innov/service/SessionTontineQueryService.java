package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.SessionTontine;
import com.it4innov.repository.SessionTontineRepository;
import com.it4innov.service.criteria.SessionTontineCriteria;
import com.it4innov.service.dto.SessionTontineDTO;
import com.it4innov.service.mapper.SessionTontineMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SessionTontine} entities in the database.
 * The main input is a {@link SessionTontineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SessionTontineDTO} or a {@link Page} of {@link SessionTontineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SessionTontineQueryService extends QueryService<SessionTontine> {

    private final Logger log = LoggerFactory.getLogger(SessionTontineQueryService.class);

    private final SessionTontineRepository sessionTontineRepository;

    private final SessionTontineMapper sessionTontineMapper;

    public SessionTontineQueryService(SessionTontineRepository sessionTontineRepository, SessionTontineMapper sessionTontineMapper) {
        this.sessionTontineRepository = sessionTontineRepository;
        this.sessionTontineMapper = sessionTontineMapper;
    }

    /**
     * Return a {@link List} of {@link SessionTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SessionTontineDTO> findByCriteria(SessionTontineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SessionTontine> specification = createSpecification(criteria);
        return sessionTontineMapper.toDto(sessionTontineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SessionTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SessionTontineDTO> findByCriteria(SessionTontineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SessionTontine> specification = createSpecification(criteria);
        return sessionTontineRepository.findAll(specification, page).map(sessionTontineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SessionTontineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SessionTontine> specification = createSpecification(criteria);
        return sessionTontineRepository.count(specification);
    }

    /**
     * Function to convert {@link SessionTontineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SessionTontine> createSpecification(SessionTontineCriteria criteria) {
        Specification<SessionTontine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SessionTontine_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), SessionTontine_.libelle));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), SessionTontine_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), SessionTontine_.dateFin));
            }
            if (criteria.getCotisationTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCotisationTontineId(),
                            root -> root.join(SessionTontine_.cotisationTontines, JoinType.LEFT).get(CotisationTontine_.id)
                        )
                    );
            }
            if (criteria.getDecaissementTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDecaissementTontineId(),
                            root -> root.join(SessionTontine_.decaissementTontines, JoinType.LEFT).get(DecaissementTontine_.id)
                        )
                    );
            }
            if (criteria.getTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTontineId(),
                            root -> root.join(SessionTontine_.tontine, JoinType.LEFT).get(Tontine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
