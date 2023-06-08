package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.FormuleAdhesion;
import com.it4innov.repository.FormuleAdhesionRepository;
import com.it4innov.service.criteria.FormuleAdhesionCriteria;
import com.it4innov.service.dto.FormuleAdhesionDTO;
import com.it4innov.service.mapper.FormuleAdhesionMapper;
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
 * Service for executing complex queries for {@link FormuleAdhesion} entities in the database.
 * The main input is a {@link FormuleAdhesionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormuleAdhesionDTO} or a {@link Page} of {@link FormuleAdhesionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormuleAdhesionQueryService extends QueryService<FormuleAdhesion> {

    private final Logger log = LoggerFactory.getLogger(FormuleAdhesionQueryService.class);

    private final FormuleAdhesionRepository formuleAdhesionRepository;

    private final FormuleAdhesionMapper formuleAdhesionMapper;

    public FormuleAdhesionQueryService(FormuleAdhesionRepository formuleAdhesionRepository, FormuleAdhesionMapper formuleAdhesionMapper) {
        this.formuleAdhesionRepository = formuleAdhesionRepository;
        this.formuleAdhesionMapper = formuleAdhesionMapper;
    }

    /**
     * Return a {@link List} of {@link FormuleAdhesionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormuleAdhesionDTO> findByCriteria(FormuleAdhesionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FormuleAdhesion> specification = createSpecification(criteria);
        return formuleAdhesionMapper.toDto(formuleAdhesionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormuleAdhesionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormuleAdhesionDTO> findByCriteria(FormuleAdhesionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FormuleAdhesion> specification = createSpecification(criteria);
        return formuleAdhesionRepository.findAll(specification, page).map(formuleAdhesionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormuleAdhesionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FormuleAdhesion> specification = createSpecification(criteria);
        return formuleAdhesionRepository.count(specification);
    }

    /**
     * Function to convert {@link FormuleAdhesionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FormuleAdhesion> createSpecification(FormuleAdhesionCriteria criteria) {
        Specification<FormuleAdhesion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FormuleAdhesion_.id));
            }
            if (criteria.getAdhesionPeriodique() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getAdhesionPeriodique(), FormuleAdhesion_.adhesionPeriodique));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), FormuleAdhesion_.dateDebut));
            }
            if (criteria.getDureeAdhesionMois() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDureeAdhesionMois(), FormuleAdhesion_.dureeAdhesionMois));
            }
            if (criteria.getMontantLibre() != null) {
                specification = specification.and(buildSpecification(criteria.getMontantLibre(), FormuleAdhesion_.montantLibre));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FormuleAdhesion_.description));
            }
            if (criteria.getTarif() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTarif(), FormuleAdhesion_.tarif));
            }
            if (criteria.getAdhesionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAdhesionId(),
                            root -> root.join(FormuleAdhesion_.adhesion, JoinType.LEFT).get(Adhesion_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
