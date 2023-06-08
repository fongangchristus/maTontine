package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Monnaie;
import com.it4innov.repository.MonnaieRepository;
import com.it4innov.service.criteria.MonnaieCriteria;
import com.it4innov.service.dto.MonnaieDTO;
import com.it4innov.service.mapper.MonnaieMapper;
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
 * Service for executing complex queries for {@link Monnaie} entities in the database.
 * The main input is a {@link MonnaieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MonnaieDTO} or a {@link Page} of {@link MonnaieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonnaieQueryService extends QueryService<Monnaie> {

    private final Logger log = LoggerFactory.getLogger(MonnaieQueryService.class);

    private final MonnaieRepository monnaieRepository;

    private final MonnaieMapper monnaieMapper;

    public MonnaieQueryService(MonnaieRepository monnaieRepository, MonnaieMapper monnaieMapper) {
        this.monnaieRepository = monnaieRepository;
        this.monnaieMapper = monnaieMapper;
    }

    /**
     * Return a {@link List} of {@link MonnaieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MonnaieDTO> findByCriteria(MonnaieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Monnaie> specification = createSpecification(criteria);
        return monnaieMapper.toDto(monnaieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MonnaieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonnaieDTO> findByCriteria(MonnaieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Monnaie> specification = createSpecification(criteria);
        return monnaieRepository.findAll(specification, page).map(monnaieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonnaieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Monnaie> specification = createSpecification(criteria);
        return monnaieRepository.count(specification);
    }

    /**
     * Function to convert {@link MonnaieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Monnaie> createSpecification(MonnaieCriteria criteria) {
        Specification<Monnaie> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Monnaie_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Monnaie_.libele));
            }
            if (criteria.getAssociationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssociationId(),
                            root -> root.join(Monnaie_.associations, JoinType.LEFT).get(Association_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
