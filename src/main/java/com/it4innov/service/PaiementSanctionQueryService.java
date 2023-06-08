package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.PaiementSanction;
import com.it4innov.repository.PaiementSanctionRepository;
import com.it4innov.service.criteria.PaiementSanctionCriteria;
import com.it4innov.service.dto.PaiementSanctionDTO;
import com.it4innov.service.mapper.PaiementSanctionMapper;
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
 * Service for executing complex queries for {@link PaiementSanction} entities in the database.
 * The main input is a {@link PaiementSanctionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaiementSanctionDTO} or a {@link Page} of {@link PaiementSanctionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaiementSanctionQueryService extends QueryService<PaiementSanction> {

    private final Logger log = LoggerFactory.getLogger(PaiementSanctionQueryService.class);

    private final PaiementSanctionRepository paiementSanctionRepository;

    private final PaiementSanctionMapper paiementSanctionMapper;

    public PaiementSanctionQueryService(
        PaiementSanctionRepository paiementSanctionRepository,
        PaiementSanctionMapper paiementSanctionMapper
    ) {
        this.paiementSanctionRepository = paiementSanctionRepository;
        this.paiementSanctionMapper = paiementSanctionMapper;
    }

    /**
     * Return a {@link List} of {@link PaiementSanctionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementSanctionDTO> findByCriteria(PaiementSanctionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaiementSanction> specification = createSpecification(criteria);
        return paiementSanctionMapper.toDto(paiementSanctionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaiementSanctionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementSanctionDTO> findByCriteria(PaiementSanctionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaiementSanction> specification = createSpecification(criteria);
        return paiementSanctionRepository.findAll(specification, page).map(paiementSanctionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaiementSanctionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaiementSanction> specification = createSpecification(criteria);
        return paiementSanctionRepository.count(specification);
    }

    /**
     * Function to convert {@link PaiementSanctionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaiementSanction> createSpecification(PaiementSanctionCriteria criteria) {
        Specification<PaiementSanction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaiementSanction_.id));
            }
            if (criteria.getReferencePaiement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReferencePaiement(), PaiementSanction_.referencePaiement));
            }
            if (criteria.getSanctionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSanctionId(),
                            root -> root.join(PaiementSanction_.sanction, JoinType.LEFT).get(Sanction_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
