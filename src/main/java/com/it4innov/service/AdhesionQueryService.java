package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Adhesion;
import com.it4innov.repository.AdhesionRepository;
import com.it4innov.service.criteria.AdhesionCriteria;
import com.it4innov.service.dto.AdhesionDTO;
import com.it4innov.service.mapper.AdhesionMapper;
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
 * Service for executing complex queries for {@link Adhesion} entities in the database.
 * The main input is a {@link AdhesionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdhesionDTO} or a {@link Page} of {@link AdhesionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdhesionQueryService extends QueryService<Adhesion> {

    private final Logger log = LoggerFactory.getLogger(AdhesionQueryService.class);

    private final AdhesionRepository adhesionRepository;

    private final AdhesionMapper adhesionMapper;

    public AdhesionQueryService(AdhesionRepository adhesionRepository, AdhesionMapper adhesionMapper) {
        this.adhesionRepository = adhesionRepository;
        this.adhesionMapper = adhesionMapper;
    }

    /**
     * Return a {@link List} of {@link AdhesionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdhesionDTO> findByCriteria(AdhesionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Adhesion> specification = createSpecification(criteria);
        return adhesionMapper.toDto(adhesionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdhesionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdhesionDTO> findByCriteria(AdhesionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Adhesion> specification = createSpecification(criteria);
        return adhesionRepository.findAll(specification, page).map(adhesionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdhesionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Adhesion> specification = createSpecification(criteria);
        return adhesionRepository.count(specification);
    }

    /**
     * Function to convert {@link AdhesionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Adhesion> createSpecification(AdhesionCriteria criteria) {
        Specification<Adhesion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Adhesion_.id));
            }
            if (criteria.getStatutAdhesion() != null) {
                specification = specification.and(buildSpecification(criteria.getStatutAdhesion(), Adhesion_.statutAdhesion));
            }
            if (criteria.getMatriculePersonne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatriculePersonne(), Adhesion_.matriculePersonne));
            }
            if (criteria.getDateDebutAdhesion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebutAdhesion(), Adhesion_.dateDebutAdhesion));
            }
            if (criteria.getDateFinAdhesion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFinAdhesion(), Adhesion_.dateFinAdhesion));
            }
            if (criteria.getFormuleAdhesionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFormuleAdhesionId(),
                            root -> root.join(Adhesion_.formuleAdhesions, JoinType.LEFT).get(FormuleAdhesion_.id)
                        )
                    );
            }
            if (criteria.getPaiementAdhesionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaiementAdhesionId(),
                            root -> root.join(Adhesion_.paiementAdhesions, JoinType.LEFT).get(PaiementAdhesion_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
