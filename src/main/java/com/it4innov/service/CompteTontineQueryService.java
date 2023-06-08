package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.CompteTontine;
import com.it4innov.repository.CompteTontineRepository;
import com.it4innov.service.criteria.CompteTontineCriteria;
import com.it4innov.service.dto.CompteTontineDTO;
import com.it4innov.service.mapper.CompteTontineMapper;
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
 * Service for executing complex queries for {@link CompteTontine} entities in the database.
 * The main input is a {@link CompteTontineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompteTontineDTO} or a {@link Page} of {@link CompteTontineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompteTontineQueryService extends QueryService<CompteTontine> {

    private final Logger log = LoggerFactory.getLogger(CompteTontineQueryService.class);

    private final CompteTontineRepository compteTontineRepository;

    private final CompteTontineMapper compteTontineMapper;

    public CompteTontineQueryService(CompteTontineRepository compteTontineRepository, CompteTontineMapper compteTontineMapper) {
        this.compteTontineRepository = compteTontineRepository;
        this.compteTontineMapper = compteTontineMapper;
    }

    /**
     * Return a {@link List} of {@link CompteTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompteTontineDTO> findByCriteria(CompteTontineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CompteTontine> specification = createSpecification(criteria);
        return compteTontineMapper.toDto(compteTontineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompteTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompteTontineDTO> findByCriteria(CompteTontineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CompteTontine> specification = createSpecification(criteria);
        return compteTontineRepository.findAll(specification, page).map(compteTontineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompteTontineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CompteTontine> specification = createSpecification(criteria);
        return compteTontineRepository.count(specification);
    }

    /**
     * Function to convert {@link CompteTontineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CompteTontine> createSpecification(CompteTontineCriteria criteria) {
        Specification<CompteTontine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CompteTontine_.id));
            }
            if (criteria.getEtatDeCompte() != null) {
                specification = specification.and(buildSpecification(criteria.getEtatDeCompte(), CompteTontine_.etatDeCompte));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), CompteTontine_.libele));
            }
            if (criteria.getOdreBeneficiere() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOdreBeneficiere(), CompteTontine_.odreBeneficiere));
            }
            if (criteria.getMatriculeCompte() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatriculeCompte(), CompteTontine_.matriculeCompte));
            }
            if (criteria.getMatriculeMenbre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatriculeMenbre(), CompteTontine_.matriculeMenbre));
            }
            if (criteria.getTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTontineId(),
                            root -> root.join(CompteTontine_.tontine, JoinType.LEFT).get(Tontine_.id)
                        )
                    );
            }
            if (criteria.getCotisationTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCotisationTontineId(),
                            root -> root.join(CompteTontine_.cotisationTontines, JoinType.LEFT).get(CotisationTontine_.id)
                        )
                    );
            }
            if (criteria.getDecaissementTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDecaissementTontineId(),
                            root -> root.join(CompteTontine_.decaissementTontines, JoinType.LEFT).get(DecaissementTontine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
