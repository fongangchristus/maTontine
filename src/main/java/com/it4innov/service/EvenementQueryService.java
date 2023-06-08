package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Evenement;
import com.it4innov.repository.EvenementRepository;
import com.it4innov.service.criteria.EvenementCriteria;
import com.it4innov.service.dto.EvenementDTO;
import com.it4innov.service.mapper.EvenementMapper;
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
 * Service for executing complex queries for {@link Evenement} entities in the database.
 * The main input is a {@link EvenementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EvenementDTO} or a {@link Page} of {@link EvenementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvenementQueryService extends QueryService<Evenement> {

    private final Logger log = LoggerFactory.getLogger(EvenementQueryService.class);

    private final EvenementRepository evenementRepository;

    private final EvenementMapper evenementMapper;

    public EvenementQueryService(EvenementRepository evenementRepository, EvenementMapper evenementMapper) {
        this.evenementRepository = evenementRepository;
        this.evenementMapper = evenementMapper;
    }

    /**
     * Return a {@link List} of {@link EvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EvenementDTO> findByCriteria(EvenementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Evenement> specification = createSpecification(criteria);
        return evenementMapper.toDto(evenementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvenementDTO> findByCriteria(EvenementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evenement> specification = createSpecification(criteria);
        return evenementRepository.findAll(specification, page).map(evenementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvenementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Evenement> specification = createSpecification(criteria);
        return evenementRepository.count(specification);
    }

    /**
     * Function to convert {@link EvenementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evenement> createSpecification(EvenementCriteria criteria) {
        Specification<Evenement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Evenement_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Evenement_.libele));
            }
            if (criteria.getCodepot() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodepot(), Evenement_.codepot));
            }
            if (criteria.getMontantPayer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMontantPayer(), Evenement_.montantPayer));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Evenement_.description));
            }
            if (criteria.getBudget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBudget(), Evenement_.budget));
            }
            if (criteria.getDateEvenement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEvenement(), Evenement_.dateEvenement));
            }
            if (criteria.getTypeEvenementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTypeEvenementId(),
                            root -> root.join(Evenement_.typeEvenement, JoinType.LEFT).get(TypeEvenement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
