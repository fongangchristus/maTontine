package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.TypeEvenement;
import com.it4innov.repository.TypeEvenementRepository;
import com.it4innov.service.criteria.TypeEvenementCriteria;
import com.it4innov.service.dto.TypeEvenementDTO;
import com.it4innov.service.mapper.TypeEvenementMapper;
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
 * Service for executing complex queries for {@link TypeEvenement} entities in the database.
 * The main input is a {@link TypeEvenementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeEvenementDTO} or a {@link Page} of {@link TypeEvenementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeEvenementQueryService extends QueryService<TypeEvenement> {

    private final Logger log = LoggerFactory.getLogger(TypeEvenementQueryService.class);

    private final TypeEvenementRepository typeEvenementRepository;

    private final TypeEvenementMapper typeEvenementMapper;

    public TypeEvenementQueryService(TypeEvenementRepository typeEvenementRepository, TypeEvenementMapper typeEvenementMapper) {
        this.typeEvenementRepository = typeEvenementRepository;
        this.typeEvenementMapper = typeEvenementMapper;
    }

    /**
     * Return a {@link List} of {@link TypeEvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeEvenementDTO> findByCriteria(TypeEvenementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeEvenement> specification = createSpecification(criteria);
        return typeEvenementMapper.toDto(typeEvenementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypeEvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeEvenementDTO> findByCriteria(TypeEvenementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeEvenement> specification = createSpecification(criteria);
        return typeEvenementRepository.findAll(specification, page).map(typeEvenementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeEvenementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeEvenement> specification = createSpecification(criteria);
        return typeEvenementRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeEvenementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeEvenement> createSpecification(TypeEvenementCriteria criteria) {
        Specification<TypeEvenement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeEvenement_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), TypeEvenement_.libele));
            }
            if (criteria.getObservation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservation(), TypeEvenement_.observation));
            }
            if (criteria.getEvenementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEvenementId(),
                            root -> root.join(TypeEvenement_.evenements, JoinType.LEFT).get(Evenement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
