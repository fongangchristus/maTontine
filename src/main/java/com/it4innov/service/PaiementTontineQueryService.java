package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.PaiementTontine;
import com.it4innov.repository.PaiementTontineRepository;
import com.it4innov.service.criteria.PaiementTontineCriteria;
import com.it4innov.service.dto.PaiementTontineDTO;
import com.it4innov.service.mapper.PaiementTontineMapper;
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
 * Service for executing complex queries for {@link PaiementTontine} entities in the database.
 * The main input is a {@link PaiementTontineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaiementTontineDTO} or a {@link Page} of {@link PaiementTontineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaiementTontineQueryService extends QueryService<PaiementTontine> {

    private final Logger log = LoggerFactory.getLogger(PaiementTontineQueryService.class);

    private final PaiementTontineRepository paiementTontineRepository;

    private final PaiementTontineMapper paiementTontineMapper;

    public PaiementTontineQueryService(PaiementTontineRepository paiementTontineRepository, PaiementTontineMapper paiementTontineMapper) {
        this.paiementTontineRepository = paiementTontineRepository;
        this.paiementTontineMapper = paiementTontineMapper;
    }

    /**
     * Return a {@link List} of {@link PaiementTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementTontineDTO> findByCriteria(PaiementTontineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaiementTontine> specification = createSpecification(criteria);
        return paiementTontineMapper.toDto(paiementTontineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaiementTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementTontineDTO> findByCriteria(PaiementTontineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaiementTontine> specification = createSpecification(criteria);
        return paiementTontineRepository.findAll(specification, page).map(paiementTontineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaiementTontineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaiementTontine> specification = createSpecification(criteria);
        return paiementTontineRepository.count(specification);
    }

    /**
     * Function to convert {@link PaiementTontineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaiementTontine> createSpecification(PaiementTontineCriteria criteria) {
        Specification<PaiementTontine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaiementTontine_.id));
            }
            if (criteria.getReferencePaiement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReferencePaiement(), PaiementTontine_.referencePaiement));
            }
            if (criteria.getCotisationTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCotisationTontineId(),
                            root -> root.join(PaiementTontine_.cotisationTontine, JoinType.LEFT).get(CotisationTontine_.id)
                        )
                    );
            }
            if (criteria.getDecaissementTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDecaissementTontineId(),
                            root -> root.join(PaiementTontine_.decaissementTontine, JoinType.LEFT).get(DecaissementTontine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
