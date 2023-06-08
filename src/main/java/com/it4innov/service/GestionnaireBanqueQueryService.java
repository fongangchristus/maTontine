package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.GestionnaireBanque;
import com.it4innov.repository.GestionnaireBanqueRepository;
import com.it4innov.service.criteria.GestionnaireBanqueCriteria;
import com.it4innov.service.dto.GestionnaireBanqueDTO;
import com.it4innov.service.mapper.GestionnaireBanqueMapper;
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
 * Service for executing complex queries for {@link GestionnaireBanque} entities in the database.
 * The main input is a {@link GestionnaireBanqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GestionnaireBanqueDTO} or a {@link Page} of {@link GestionnaireBanqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GestionnaireBanqueQueryService extends QueryService<GestionnaireBanque> {

    private final Logger log = LoggerFactory.getLogger(GestionnaireBanqueQueryService.class);

    private final GestionnaireBanqueRepository gestionnaireBanqueRepository;

    private final GestionnaireBanqueMapper gestionnaireBanqueMapper;

    public GestionnaireBanqueQueryService(
        GestionnaireBanqueRepository gestionnaireBanqueRepository,
        GestionnaireBanqueMapper gestionnaireBanqueMapper
    ) {
        this.gestionnaireBanqueRepository = gestionnaireBanqueRepository;
        this.gestionnaireBanqueMapper = gestionnaireBanqueMapper;
    }

    /**
     * Return a {@link List} of {@link GestionnaireBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GestionnaireBanqueDTO> findByCriteria(GestionnaireBanqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GestionnaireBanque> specification = createSpecification(criteria);
        return gestionnaireBanqueMapper.toDto(gestionnaireBanqueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GestionnaireBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GestionnaireBanqueDTO> findByCriteria(GestionnaireBanqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GestionnaireBanque> specification = createSpecification(criteria);
        return gestionnaireBanqueRepository.findAll(specification, page).map(gestionnaireBanqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GestionnaireBanqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GestionnaireBanque> specification = createSpecification(criteria);
        return gestionnaireBanqueRepository.count(specification);
    }

    /**
     * Function to convert {@link GestionnaireBanqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GestionnaireBanque> createSpecification(GestionnaireBanqueCriteria criteria) {
        Specification<GestionnaireBanque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GestionnaireBanque_.id));
            }
            if (criteria.getMatriculeMembre() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMatriculeMembre(), GestionnaireBanque_.matriculeMembre));
            }
            if (criteria.getBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBanqueId(),
                            root -> root.join(GestionnaireBanque_.banque, JoinType.LEFT).get(Banque_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
