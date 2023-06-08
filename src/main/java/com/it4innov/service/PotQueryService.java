package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Pot;
import com.it4innov.repository.PotRepository;
import com.it4innov.service.criteria.PotCriteria;
import com.it4innov.service.dto.PotDTO;
import com.it4innov.service.mapper.PotMapper;
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
 * Service for executing complex queries for {@link Pot} entities in the database.
 * The main input is a {@link PotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PotDTO} or a {@link Page} of {@link PotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PotQueryService extends QueryService<Pot> {

    private final Logger log = LoggerFactory.getLogger(PotQueryService.class);

    private final PotRepository potRepository;

    private final PotMapper potMapper;

    public PotQueryService(PotRepository potRepository, PotMapper potMapper) {
        this.potRepository = potRepository;
        this.potMapper = potMapper;
    }

    /**
     * Return a {@link List} of {@link PotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PotDTO> findByCriteria(PotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pot> specification = createSpecification(criteria);
        return potMapper.toDto(potRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PotDTO> findByCriteria(PotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pot> specification = createSpecification(criteria);
        return potRepository.findAll(specification, page).map(potMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pot> specification = createSpecification(criteria);
        return potRepository.count(specification);
    }

    /**
     * Function to convert {@link PotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pot> createSpecification(PotCriteria criteria) {
        Specification<Pot> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pot_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Pot_.libele));
            }
            if (criteria.getCodepot() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodepot(), Pot_.codepot));
            }
            if (criteria.getMontantCible() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantCible(), Pot_.montantCible));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Pot_.description));
            }
            if (criteria.getDateDebutCollecte() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebutCollecte(), Pot_.dateDebutCollecte));
            }
            if (criteria.getDateFinCollecte() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFinCollecte(), Pot_.dateFinCollecte));
            }
            if (criteria.getStatut() != null) {
                specification = specification.and(buildSpecification(criteria.getStatut(), Pot_.statut));
            }
            if (criteria.getContributionPotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContributionPotId(),
                            root -> root.join(Pot_.contributionPots, JoinType.LEFT).get(ContributionPot_.id)
                        )
                    );
            }
            if (criteria.getCommentairePotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommentairePotId(),
                            root -> root.join(Pot_.commentairePots, JoinType.LEFT).get(CommentairePot_.id)
                        )
                    );
            }
            if (criteria.getTypePotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTypePotId(), root -> root.join(Pot_.typePot, JoinType.LEFT).get(TypePot_.id))
                    );
            }
        }
        return specification;
    }
}
