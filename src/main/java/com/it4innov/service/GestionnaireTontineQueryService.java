package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.GestionnaireTontine;
import com.it4innov.repository.GestionnaireTontineRepository;
import com.it4innov.service.criteria.GestionnaireTontineCriteria;
import com.it4innov.service.dto.GestionnaireTontineDTO;
import com.it4innov.service.mapper.GestionnaireTontineMapper;
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
 * Service for executing complex queries for {@link GestionnaireTontine} entities in the database.
 * The main input is a {@link GestionnaireTontineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GestionnaireTontineDTO} or a {@link Page} of {@link GestionnaireTontineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GestionnaireTontineQueryService extends QueryService<GestionnaireTontine> {

    private final Logger log = LoggerFactory.getLogger(GestionnaireTontineQueryService.class);

    private final GestionnaireTontineRepository gestionnaireTontineRepository;

    private final GestionnaireTontineMapper gestionnaireTontineMapper;

    public GestionnaireTontineQueryService(
        GestionnaireTontineRepository gestionnaireTontineRepository,
        GestionnaireTontineMapper gestionnaireTontineMapper
    ) {
        this.gestionnaireTontineRepository = gestionnaireTontineRepository;
        this.gestionnaireTontineMapper = gestionnaireTontineMapper;
    }

    /**
     * Return a {@link List} of {@link GestionnaireTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GestionnaireTontineDTO> findByCriteria(GestionnaireTontineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GestionnaireTontine> specification = createSpecification(criteria);
        return gestionnaireTontineMapper.toDto(gestionnaireTontineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GestionnaireTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GestionnaireTontineDTO> findByCriteria(GestionnaireTontineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GestionnaireTontine> specification = createSpecification(criteria);
        return gestionnaireTontineRepository.findAll(specification, page).map(gestionnaireTontineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GestionnaireTontineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GestionnaireTontine> specification = createSpecification(criteria);
        return gestionnaireTontineRepository.count(specification);
    }

    /**
     * Function to convert {@link GestionnaireTontineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GestionnaireTontine> createSpecification(GestionnaireTontineCriteria criteria) {
        Specification<GestionnaireTontine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GestionnaireTontine_.id));
            }
            if (criteria.getMatriculeAdherent() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMatriculeAdherent(), GestionnaireTontine_.matriculeAdherent));
            }
            if (criteria.getCodeTontine() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeTontine(), GestionnaireTontine_.codeTontine));
            }
            if (criteria.getDatePriseFonction() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDatePriseFonction(), GestionnaireTontine_.datePriseFonction));
            }
            if (criteria.getDateFinFonction() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDateFinFonction(), GestionnaireTontine_.dateFinFonction));
            }
            if (criteria.getTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTontineId(),
                            root -> root.join(GestionnaireTontine_.tontine, JoinType.LEFT).get(Tontine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
