package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.FonctionAdherent;
import com.it4innov.repository.FonctionAdherentRepository;
import com.it4innov.service.criteria.FonctionAdherentCriteria;
import com.it4innov.service.dto.FonctionAdherentDTO;
import com.it4innov.service.mapper.FonctionAdherentMapper;
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
 * Service for executing complex queries for {@link FonctionAdherent} entities in the database.
 * The main input is a {@link FonctionAdherentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FonctionAdherentDTO} or a {@link Page} of {@link FonctionAdherentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FonctionAdherentQueryService extends QueryService<FonctionAdherent> {

    private final Logger log = LoggerFactory.getLogger(FonctionAdherentQueryService.class);

    private final FonctionAdherentRepository fonctionAdherentRepository;

    private final FonctionAdherentMapper fonctionAdherentMapper;

    public FonctionAdherentQueryService(
        FonctionAdherentRepository fonctionAdherentRepository,
        FonctionAdherentMapper fonctionAdherentMapper
    ) {
        this.fonctionAdherentRepository = fonctionAdherentRepository;
        this.fonctionAdherentMapper = fonctionAdherentMapper;
    }

    /**
     * Return a {@link List} of {@link FonctionAdherentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FonctionAdherentDTO> findByCriteria(FonctionAdherentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FonctionAdherent> specification = createSpecification(criteria);
        return fonctionAdherentMapper.toDto(fonctionAdherentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FonctionAdherentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FonctionAdherentDTO> findByCriteria(FonctionAdherentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FonctionAdherent> specification = createSpecification(criteria);
        return fonctionAdherentRepository.findAll(specification, page).map(fonctionAdherentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FonctionAdherentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FonctionAdherent> specification = createSpecification(criteria);
        return fonctionAdherentRepository.count(specification);
    }

    /**
     * Function to convert {@link FonctionAdherentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FonctionAdherent> createSpecification(FonctionAdherentCriteria criteria) {
        Specification<FonctionAdherent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FonctionAdherent_.id));
            }
            if (criteria.getMatriculeAdherent() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMatriculeAdherent(), FonctionAdherent_.matriculeAdherent));
            }
            if (criteria.getCodeFonction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeFonction(), FonctionAdherent_.codeFonction));
            }
            if (criteria.getDatePriseFonction() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDatePriseFonction(), FonctionAdherent_.datePriseFonction));
            }
            if (criteria.getDateFinFonction() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDateFinFonction(), FonctionAdherent_.dateFinFonction));
            }
            if (criteria.getActif() != null) {
                specification = specification.and(buildSpecification(criteria.getActif(), FonctionAdherent_.actif));
            }
            if (criteria.getAdherentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAdherentId(),
                            root -> root.join(FonctionAdherent_.adherent, JoinType.LEFT).get(Personne_.id)
                        )
                    );
            }
            if (criteria.getFonctionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFonctionId(),
                            root -> root.join(FonctionAdherent_.fonction, JoinType.LEFT).get(Fonction_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
