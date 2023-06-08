package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.DecaisementBanque;
import com.it4innov.repository.DecaisementBanqueRepository;
import com.it4innov.service.criteria.DecaisementBanqueCriteria;
import com.it4innov.service.dto.DecaisementBanqueDTO;
import com.it4innov.service.mapper.DecaisementBanqueMapper;
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
 * Service for executing complex queries for {@link DecaisementBanque} entities in the database.
 * The main input is a {@link DecaisementBanqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DecaisementBanqueDTO} or a {@link Page} of {@link DecaisementBanqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DecaisementBanqueQueryService extends QueryService<DecaisementBanque> {

    private final Logger log = LoggerFactory.getLogger(DecaisementBanqueQueryService.class);

    private final DecaisementBanqueRepository decaisementBanqueRepository;

    private final DecaisementBanqueMapper decaisementBanqueMapper;

    public DecaisementBanqueQueryService(
        DecaisementBanqueRepository decaisementBanqueRepository,
        DecaisementBanqueMapper decaisementBanqueMapper
    ) {
        this.decaisementBanqueRepository = decaisementBanqueRepository;
        this.decaisementBanqueMapper = decaisementBanqueMapper;
    }

    /**
     * Return a {@link List} of {@link DecaisementBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DecaisementBanqueDTO> findByCriteria(DecaisementBanqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DecaisementBanque> specification = createSpecification(criteria);
        return decaisementBanqueMapper.toDto(decaisementBanqueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DecaisementBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DecaisementBanqueDTO> findByCriteria(DecaisementBanqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DecaisementBanque> specification = createSpecification(criteria);
        return decaisementBanqueRepository.findAll(specification, page).map(decaisementBanqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DecaisementBanqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DecaisementBanque> specification = createSpecification(criteria);
        return decaisementBanqueRepository.count(specification);
    }

    /**
     * Function to convert {@link DecaisementBanqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DecaisementBanque> createSpecification(DecaisementBanqueCriteria criteria) {
        Specification<DecaisementBanque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DecaisementBanque_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), DecaisementBanque_.libelle));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), DecaisementBanque_.montant));
            }
            if (criteria.getDateDecaissement() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDateDecaissement(), DecaisementBanque_.dateDecaissement));
            }
            if (criteria.getMontantDecaisse() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMontantDecaisse(), DecaisementBanque_.montantDecaisse));
            }
            if (criteria.getCommentaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommentaire(), DecaisementBanque_.commentaire));
            }
            if (criteria.getCompteBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompteBanqueId(),
                            root -> root.join(DecaisementBanque_.compteBanque, JoinType.LEFT).get(CompteBanque_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
