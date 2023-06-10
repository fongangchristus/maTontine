package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Banque;
import com.it4innov.repository.BanqueRepository;
import com.it4innov.service.criteria.BanqueCriteria;
import com.it4innov.service.dto.BanqueDTO;
import com.it4innov.service.mapper.BanqueMapper;
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
 * Service for executing complex queries for {@link Banque} entities in the database.
 * The main input is a {@link BanqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BanqueDTO} or a {@link Page} of {@link BanqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BanqueQueryService extends QueryService<Banque> {

    private final Logger log = LoggerFactory.getLogger(BanqueQueryService.class);

    private final BanqueRepository banqueRepository;

    private final BanqueMapper banqueMapper;

    public BanqueQueryService(BanqueRepository banqueRepository, BanqueMapper banqueMapper) {
        this.banqueRepository = banqueRepository;
        this.banqueMapper = banqueMapper;
    }

    /**
     * Return a {@link List} of {@link BanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BanqueDTO> findByCriteria(BanqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Banque> specification = createSpecification(criteria);
        return banqueMapper.toDto(banqueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BanqueDTO> findByCriteria(BanqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Banque> specification = createSpecification(criteria);
        return banqueRepository.findAll(specification, page).map(banqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BanqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Banque> specification = createSpecification(criteria);
        return banqueRepository.count(specification);
    }

    /**
     * Function to convert {@link BanqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Banque> createSpecification(BanqueCriteria criteria) {
        Specification<Banque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Banque_.id));
            }
            if (criteria.getCodeAssociation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeAssociation(), Banque_.codeAssociation));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Banque_.libelle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Banque_.description));
            }
            if (criteria.getDateOuverture() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOuverture(), Banque_.dateOuverture));
            }
            if (criteria.getDateCloture() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCloture(), Banque_.dateCloture));
            }
            if (criteria.getPenaliteRetardRnbrsmnt() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPenaliteRetardRnbrsmnt(), Banque_.penaliteRetardRnbrsmnt));
            }
            if (criteria.getTauxInteretPret() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTauxInteretPret(), Banque_.tauxInteretPret));
            }
            if (criteria.getCompteBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompteBanqueId(),
                            root -> root.join(Banque_.compteBanques, JoinType.LEFT).get(CompteBanque_.id)
                        )
                    );
            }
            if (criteria.getGestionnaireBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGestionnaireBanqueId(),
                            root -> root.join(Banque_.gestionnaireBanques, JoinType.LEFT).get(GestionnaireBanque_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
