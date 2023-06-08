package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Association;
import com.it4innov.repository.AssociationRepository;
import com.it4innov.service.criteria.AssociationCriteria;
import com.it4innov.service.dto.AssociationDTO;
import com.it4innov.service.mapper.AssociationMapper;
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
 * Service for executing complex queries for {@link Association} entities in the database.
 * The main input is a {@link AssociationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssociationDTO} or a {@link Page} of {@link AssociationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssociationQueryService extends QueryService<Association> {

    private final Logger log = LoggerFactory.getLogger(AssociationQueryService.class);

    private final AssociationRepository associationRepository;

    private final AssociationMapper associationMapper;

    public AssociationQueryService(AssociationRepository associationRepository, AssociationMapper associationMapper) {
        this.associationRepository = associationRepository;
        this.associationMapper = associationMapper;
    }

    /**
     * Return a {@link List} of {@link AssociationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssociationDTO> findByCriteria(AssociationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Association> specification = createSpecification(criteria);
        return associationMapper.toDto(associationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssociationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssociationDTO> findByCriteria(AssociationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Association> specification = createSpecification(criteria);
        return associationRepository.findAll(specification, page).map(associationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssociationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Association> specification = createSpecification(criteria);
        return associationRepository.count(specification);
    }

    /**
     * Function to convert {@link AssociationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Association> createSpecification(AssociationCriteria criteria) {
        Specification<Association> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Association_.id));
            }
            if (criteria.getCodeAssociation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeAssociation(), Association_.codeAssociation));
            }
            if (criteria.getDenomination() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDenomination(), Association_.denomination));
            }
            if (criteria.getSlogan() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlogan(), Association_.slogan));
            }
            if (criteria.getLogoPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogoPath(), Association_.logoPath));
            }
            if (criteria.getReglementPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReglementPath(), Association_.reglementPath));
            }
            if (criteria.getStatutPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatutPath(), Association_.statutPath));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Association_.description));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Association_.dateCreation));
            }
            if (criteria.getFuseauHoraire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFuseauHoraire(), Association_.fuseauHoraire));
            }
            if (criteria.getLangue() != null) {
                specification = specification.and(buildSpecification(criteria.getLangue(), Association_.langue));
            }
            if (criteria.getPresentation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPresentation(), Association_.presentation));
            }
            if (criteria.getExerciseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExerciseId(),
                            root -> root.join(Association_.exercises, JoinType.LEFT).get(Exercise_.id)
                        )
                    );
            }
            if (criteria.getDocumentAssociationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocumentAssociationId(),
                            root -> root.join(Association_.documentAssociations, JoinType.LEFT).get(DocumentAssociation_.id)
                        )
                    );
            }
            if (criteria.getMonnaieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMonnaieId(), root -> root.join(Association_.monnaie, JoinType.LEFT).get(Monnaie_.id))
                    );
            }
        }
        return specification;
    }
}
