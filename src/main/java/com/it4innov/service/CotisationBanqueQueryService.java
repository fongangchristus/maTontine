package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.CotisationBanque;
import com.it4innov.repository.CotisationBanqueRepository;
import com.it4innov.service.criteria.CotisationBanqueCriteria;
import com.it4innov.service.dto.CotisationBanqueDTO;
import com.it4innov.service.mapper.CotisationBanqueMapper;
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
 * Service for executing complex queries for {@link CotisationBanque} entities in the database.
 * The main input is a {@link CotisationBanqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CotisationBanqueDTO} or a {@link Page} of {@link CotisationBanqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CotisationBanqueQueryService extends QueryService<CotisationBanque> {

    private final Logger log = LoggerFactory.getLogger(CotisationBanqueQueryService.class);

    private final CotisationBanqueRepository cotisationBanqueRepository;

    private final CotisationBanqueMapper cotisationBanqueMapper;

    public CotisationBanqueQueryService(
        CotisationBanqueRepository cotisationBanqueRepository,
        CotisationBanqueMapper cotisationBanqueMapper
    ) {
        this.cotisationBanqueRepository = cotisationBanqueRepository;
        this.cotisationBanqueMapper = cotisationBanqueMapper;
    }

    /**
     * Return a {@link List} of {@link CotisationBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CotisationBanqueDTO> findByCriteria(CotisationBanqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CotisationBanque> specification = createSpecification(criteria);
        return cotisationBanqueMapper.toDto(cotisationBanqueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CotisationBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CotisationBanqueDTO> findByCriteria(CotisationBanqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CotisationBanque> specification = createSpecification(criteria);
        return cotisationBanqueRepository.findAll(specification, page).map(cotisationBanqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CotisationBanqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CotisationBanque> specification = createSpecification(criteria);
        return cotisationBanqueRepository.count(specification);
    }

    /**
     * Function to convert {@link CotisationBanqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CotisationBanque> createSpecification(CotisationBanqueCriteria criteria) {
        Specification<CotisationBanque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CotisationBanque_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), CotisationBanque_.libelle));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), CotisationBanque_.montant));
            }
            if (criteria.getDateCotisation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCotisation(), CotisationBanque_.dateCotisation));
            }
            if (criteria.getMontantCotise() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantCotise(), CotisationBanque_.montantCotise));
            }
            if (criteria.getCommentaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommentaire(), CotisationBanque_.commentaire));
            }
            if (criteria.getCompteBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompteBanqueId(),
                            root -> root.join(CotisationBanque_.compteBanque, JoinType.LEFT).get(CompteBanque_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
