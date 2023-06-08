package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.DecaissementTontine;
import com.it4innov.repository.DecaissementTontineRepository;
import com.it4innov.service.criteria.DecaissementTontineCriteria;
import com.it4innov.service.dto.DecaissementTontineDTO;
import com.it4innov.service.mapper.DecaissementTontineMapper;
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
 * Service for executing complex queries for {@link DecaissementTontine} entities in the database.
 * The main input is a {@link DecaissementTontineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DecaissementTontineDTO} or a {@link Page} of {@link DecaissementTontineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DecaissementTontineQueryService extends QueryService<DecaissementTontine> {

    private final Logger log = LoggerFactory.getLogger(DecaissementTontineQueryService.class);

    private final DecaissementTontineRepository decaissementTontineRepository;

    private final DecaissementTontineMapper decaissementTontineMapper;

    public DecaissementTontineQueryService(
        DecaissementTontineRepository decaissementTontineRepository,
        DecaissementTontineMapper decaissementTontineMapper
    ) {
        this.decaissementTontineRepository = decaissementTontineRepository;
        this.decaissementTontineMapper = decaissementTontineMapper;
    }

    /**
     * Return a {@link List} of {@link DecaissementTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DecaissementTontineDTO> findByCriteria(DecaissementTontineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DecaissementTontine> specification = createSpecification(criteria);
        return decaissementTontineMapper.toDto(decaissementTontineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DecaissementTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DecaissementTontineDTO> findByCriteria(DecaissementTontineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DecaissementTontine> specification = createSpecification(criteria);
        return decaissementTontineRepository.findAll(specification, page).map(decaissementTontineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DecaissementTontineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DecaissementTontine> specification = createSpecification(criteria);
        return decaissementTontineRepository.count(specification);
    }

    /**
     * Function to convert {@link DecaissementTontineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DecaissementTontine> createSpecification(DecaissementTontineCriteria criteria) {
        Specification<DecaissementTontine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DecaissementTontine_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), DecaissementTontine_.libele));
            }
            if (criteria.getDateDecaissement() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDateDecaissement(), DecaissementTontine_.dateDecaissement));
            }
            if (criteria.getMontantDecaisse() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMontantDecaisse(), DecaissementTontine_.montantDecaisse));
            }
            if (criteria.getCommentaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommentaire(), DecaissementTontine_.commentaire));
            }
            if (criteria.getPaiementTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaiementTontineId(),
                            root -> root.join(DecaissementTontine_.paiementTontines, JoinType.LEFT).get(PaiementTontine_.id)
                        )
                    );
            }
            if (criteria.getTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTontineId(),
                            root -> root.join(DecaissementTontine_.tontine, JoinType.LEFT).get(SessionTontine_.id)
                        )
                    );
            }
            if (criteria.getCompteTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompteTontineId(),
                            root -> root.join(DecaissementTontine_.compteTontine, JoinType.LEFT).get(CompteTontine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
