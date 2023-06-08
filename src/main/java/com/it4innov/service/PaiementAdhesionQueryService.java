package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.PaiementAdhesion;
import com.it4innov.repository.PaiementAdhesionRepository;
import com.it4innov.service.criteria.PaiementAdhesionCriteria;
import com.it4innov.service.dto.PaiementAdhesionDTO;
import com.it4innov.service.mapper.PaiementAdhesionMapper;
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
 * Service for executing complex queries for {@link PaiementAdhesion} entities in the database.
 * The main input is a {@link PaiementAdhesionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaiementAdhesionDTO} or a {@link Page} of {@link PaiementAdhesionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaiementAdhesionQueryService extends QueryService<PaiementAdhesion> {

    private final Logger log = LoggerFactory.getLogger(PaiementAdhesionQueryService.class);

    private final PaiementAdhesionRepository paiementAdhesionRepository;

    private final PaiementAdhesionMapper paiementAdhesionMapper;

    public PaiementAdhesionQueryService(
        PaiementAdhesionRepository paiementAdhesionRepository,
        PaiementAdhesionMapper paiementAdhesionMapper
    ) {
        this.paiementAdhesionRepository = paiementAdhesionRepository;
        this.paiementAdhesionMapper = paiementAdhesionMapper;
    }

    /**
     * Return a {@link List} of {@link PaiementAdhesionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementAdhesionDTO> findByCriteria(PaiementAdhesionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaiementAdhesion> specification = createSpecification(criteria);
        return paiementAdhesionMapper.toDto(paiementAdhesionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaiementAdhesionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementAdhesionDTO> findByCriteria(PaiementAdhesionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaiementAdhesion> specification = createSpecification(criteria);
        return paiementAdhesionRepository.findAll(specification, page).map(paiementAdhesionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaiementAdhesionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaiementAdhesion> specification = createSpecification(criteria);
        return paiementAdhesionRepository.count(specification);
    }

    /**
     * Function to convert {@link PaiementAdhesionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaiementAdhesion> createSpecification(PaiementAdhesionCriteria criteria) {
        Specification<PaiementAdhesion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaiementAdhesion_.id));
            }
            if (criteria.getReferencePaiement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReferencePaiement(), PaiementAdhesion_.referencePaiement));
            }
            if (criteria.getAdhesionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAdhesionId(),
                            root -> root.join(PaiementAdhesion_.adhesion, JoinType.LEFT).get(Adhesion_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
