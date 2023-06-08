package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Sanction;
import com.it4innov.repository.SanctionRepository;
import com.it4innov.service.criteria.SanctionCriteria;
import com.it4innov.service.dto.SanctionDTO;
import com.it4innov.service.mapper.SanctionMapper;
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
 * Service for executing complex queries for {@link Sanction} entities in the database.
 * The main input is a {@link SanctionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SanctionDTO} or a {@link Page} of {@link SanctionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SanctionQueryService extends QueryService<Sanction> {

    private final Logger log = LoggerFactory.getLogger(SanctionQueryService.class);

    private final SanctionRepository sanctionRepository;

    private final SanctionMapper sanctionMapper;

    public SanctionQueryService(SanctionRepository sanctionRepository, SanctionMapper sanctionMapper) {
        this.sanctionRepository = sanctionRepository;
        this.sanctionMapper = sanctionMapper;
    }

    /**
     * Return a {@link List} of {@link SanctionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SanctionDTO> findByCriteria(SanctionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sanction> specification = createSpecification(criteria);
        return sanctionMapper.toDto(sanctionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SanctionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SanctionDTO> findByCriteria(SanctionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sanction> specification = createSpecification(criteria);
        return sanctionRepository.findAll(specification, page).map(sanctionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SanctionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sanction> specification = createSpecification(criteria);
        return sanctionRepository.count(specification);
    }

    /**
     * Function to convert {@link SanctionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sanction> createSpecification(SanctionCriteria criteria) {
        Specification<Sanction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sanction_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Sanction_.libelle));
            }
            if (criteria.getMatriculeAdherent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatriculeAdherent(), Sanction_.matriculeAdherent));
            }
            if (criteria.getDateSanction() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateSanction(), Sanction_.dateSanction));
            }
            if (criteria.getMotifSanction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotifSanction(), Sanction_.motifSanction));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Sanction_.description));
            }
            if (criteria.getCodeActivite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeActivite(), Sanction_.codeActivite));
            }
            if (criteria.getPaiementSanctionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaiementSanctionId(),
                            root -> root.join(Sanction_.paiementSanctions, JoinType.LEFT).get(PaiementSanction_.id)
                        )
                    );
            }
            if (criteria.getSanctionConfigId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSanctionConfigId(),
                            root -> root.join(Sanction_.sanctionConfig, JoinType.LEFT).get(SanctionConfiguration_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
