package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.ContributionPot;
import com.it4innov.repository.ContributionPotRepository;
import com.it4innov.service.criteria.ContributionPotCriteria;
import com.it4innov.service.dto.ContributionPotDTO;
import com.it4innov.service.mapper.ContributionPotMapper;
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
 * Service for executing complex queries for {@link ContributionPot} entities in the database.
 * The main input is a {@link ContributionPotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContributionPotDTO} or a {@link Page} of {@link ContributionPotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContributionPotQueryService extends QueryService<ContributionPot> {

    private final Logger log = LoggerFactory.getLogger(ContributionPotQueryService.class);

    private final ContributionPotRepository contributionPotRepository;

    private final ContributionPotMapper contributionPotMapper;

    public ContributionPotQueryService(ContributionPotRepository contributionPotRepository, ContributionPotMapper contributionPotMapper) {
        this.contributionPotRepository = contributionPotRepository;
        this.contributionPotMapper = contributionPotMapper;
    }

    /**
     * Return a {@link List} of {@link ContributionPotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContributionPotDTO> findByCriteria(ContributionPotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContributionPot> specification = createSpecification(criteria);
        return contributionPotMapper.toDto(contributionPotRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContributionPotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContributionPotDTO> findByCriteria(ContributionPotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContributionPot> specification = createSpecification(criteria);
        return contributionPotRepository.findAll(specification, page).map(contributionPotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContributionPotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContributionPot> specification = createSpecification(criteria);
        return contributionPotRepository.count(specification);
    }

    /**
     * Function to convert {@link ContributionPotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContributionPot> createSpecification(ContributionPotCriteria criteria) {
        Specification<ContributionPot> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContributionPot_.id));
            }
            if (criteria.getIdentifiant() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentifiant(), ContributionPot_.identifiant));
            }
            if (criteria.getMatriculeContributeur() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getMatriculeContributeur(), ContributionPot_.matriculeContributeur)
                    );
            }
            if (criteria.getMontantContribution() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMontantContribution(), ContributionPot_.montantContribution));
            }
            if (criteria.getDateContribution() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDateContribution(), ContributionPot_.dateContribution));
            }
            if (criteria.getPotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPotId(), root -> root.join(ContributionPot_.pot, JoinType.LEFT).get(Pot_.id))
                    );
            }
        }
        return specification;
    }
}
