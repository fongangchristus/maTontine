package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.CompteBanque;
import com.it4innov.repository.CompteBanqueRepository;
import com.it4innov.service.criteria.CompteBanqueCriteria;
import com.it4innov.service.dto.CompteBanqueDTO;
import com.it4innov.service.mapper.CompteBanqueMapper;
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
 * Service for executing complex queries for {@link CompteBanque} entities in the database.
 * The main input is a {@link CompteBanqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompteBanqueDTO} or a {@link Page} of {@link CompteBanqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompteBanqueQueryService extends QueryService<CompteBanque> {

    private final Logger log = LoggerFactory.getLogger(CompteBanqueQueryService.class);

    private final CompteBanqueRepository compteBanqueRepository;

    private final CompteBanqueMapper compteBanqueMapper;

    public CompteBanqueQueryService(CompteBanqueRepository compteBanqueRepository, CompteBanqueMapper compteBanqueMapper) {
        this.compteBanqueRepository = compteBanqueRepository;
        this.compteBanqueMapper = compteBanqueMapper;
    }

    /**
     * Return a {@link List} of {@link CompteBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompteBanqueDTO> findByCriteria(CompteBanqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CompteBanque> specification = createSpecification(criteria);
        return compteBanqueMapper.toDto(compteBanqueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompteBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompteBanqueDTO> findByCriteria(CompteBanqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CompteBanque> specification = createSpecification(criteria);
        return compteBanqueRepository.findAll(specification, page).map(compteBanqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompteBanqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CompteBanque> specification = createSpecification(criteria);
        return compteBanqueRepository.count(specification);
    }

    /**
     * Function to convert {@link CompteBanqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CompteBanque> createSpecification(CompteBanqueCriteria criteria) {
        Specification<CompteBanque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CompteBanque_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), CompteBanque_.libelle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CompteBanque_.description));
            }
            if (criteria.getMatriculeAdherant() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMatriculeAdherant(), CompteBanque_.matriculeAdherant));
            }
            if (criteria.getMontantDisponnible() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMontantDisponnible(), CompteBanque_.montantDisponnible));
            }
            if (criteria.getCotisationBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCotisationBanqueId(),
                            root -> root.join(CompteBanque_.cotisationBanques, JoinType.LEFT).get(CotisationBanque_.id)
                        )
                    );
            }
            if (criteria.getDecaisementBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDecaisementBanqueId(),
                            root -> root.join(CompteBanque_.decaisementBanques, JoinType.LEFT).get(DecaisementBanque_.id)
                        )
                    );
            }
            if (criteria.getBanqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBanqueId(), root -> root.join(CompteBanque_.banque, JoinType.LEFT).get(Banque_.id))
                    );
            }
        }
        return specification;
    }
}
