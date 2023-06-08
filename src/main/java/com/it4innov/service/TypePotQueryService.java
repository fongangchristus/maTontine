package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.TypePot;
import com.it4innov.repository.TypePotRepository;
import com.it4innov.service.criteria.TypePotCriteria;
import com.it4innov.service.dto.TypePotDTO;
import com.it4innov.service.mapper.TypePotMapper;
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
 * Service for executing complex queries for {@link TypePot} entities in the database.
 * The main input is a {@link TypePotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypePotDTO} or a {@link Page} of {@link TypePotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypePotQueryService extends QueryService<TypePot> {

    private final Logger log = LoggerFactory.getLogger(TypePotQueryService.class);

    private final TypePotRepository typePotRepository;

    private final TypePotMapper typePotMapper;

    public TypePotQueryService(TypePotRepository typePotRepository, TypePotMapper typePotMapper) {
        this.typePotRepository = typePotRepository;
        this.typePotMapper = typePotMapper;
    }

    /**
     * Return a {@link List} of {@link TypePotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypePotDTO> findByCriteria(TypePotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypePot> specification = createSpecification(criteria);
        return typePotMapper.toDto(typePotRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypePotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypePotDTO> findByCriteria(TypePotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypePot> specification = createSpecification(criteria);
        return typePotRepository.findAll(specification, page).map(typePotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypePotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypePot> specification = createSpecification(criteria);
        return typePotRepository.count(specification);
    }

    /**
     * Function to convert {@link TypePotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypePot> createSpecification(TypePotCriteria criteria) {
        Specification<TypePot> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypePot_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), TypePot_.libele));
            }
            if (criteria.getDescrption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescrption(), TypePot_.descrption));
            }
            if (criteria.getPotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPotId(), root -> root.join(TypePot_.pots, JoinType.LEFT).get(Pot_.id))
                    );
            }
        }
        return specification;
    }
}
