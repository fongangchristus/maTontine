package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.HistoriquePersonne;
import com.it4innov.repository.HistoriquePersonneRepository;
import com.it4innov.service.criteria.HistoriquePersonneCriteria;
import com.it4innov.service.dto.HistoriquePersonneDTO;
import com.it4innov.service.mapper.HistoriquePersonneMapper;
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
 * Service for executing complex queries for {@link HistoriquePersonne} entities in the database.
 * The main input is a {@link HistoriquePersonneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoriquePersonneDTO} or a {@link Page} of {@link HistoriquePersonneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriquePersonneQueryService extends QueryService<HistoriquePersonne> {

    private final Logger log = LoggerFactory.getLogger(HistoriquePersonneQueryService.class);

    private final HistoriquePersonneRepository historiquePersonneRepository;

    private final HistoriquePersonneMapper historiquePersonneMapper;

    public HistoriquePersonneQueryService(
        HistoriquePersonneRepository historiquePersonneRepository,
        HistoriquePersonneMapper historiquePersonneMapper
    ) {
        this.historiquePersonneRepository = historiquePersonneRepository;
        this.historiquePersonneMapper = historiquePersonneMapper;
    }

    /**
     * Return a {@link List} of {@link HistoriquePersonneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoriquePersonneDTO> findByCriteria(HistoriquePersonneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistoriquePersonne> specification = createSpecification(criteria);
        return historiquePersonneMapper.toDto(historiquePersonneRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HistoriquePersonneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriquePersonneDTO> findByCriteria(HistoriquePersonneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriquePersonne> specification = createSpecification(criteria);
        return historiquePersonneRepository.findAll(specification, page).map(historiquePersonneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriquePersonneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistoriquePersonne> specification = createSpecification(criteria);
        return historiquePersonneRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriquePersonneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriquePersonne> createSpecification(HistoriquePersonneCriteria criteria) {
        Specification<HistoriquePersonne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HistoriquePersonne_.id));
            }
            if (criteria.getDateAction() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAction(), HistoriquePersonne_.dateAction));
            }
            if (criteria.getMatriculePersonne() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMatriculePersonne(), HistoriquePersonne_.matriculePersonne));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAction(), HistoriquePersonne_.action));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResult(), HistoriquePersonne_.result));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HistoriquePersonne_.description));
            }
            if (criteria.getPersonneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPersonneId(),
                            root -> root.join(HistoriquePersonne_.personne, JoinType.LEFT).get(Personne_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
