package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.PaiementBanque;
import com.it4innov.repository.PaiementBanqueRepository;
import com.it4innov.service.criteria.PaiementBanqueCriteria;
import com.it4innov.service.dto.PaiementBanqueDTO;
import com.it4innov.service.mapper.PaiementBanqueMapper;
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
 * Service for executing complex queries for {@link PaiementBanque} entities in the database.
 * The main input is a {@link PaiementBanqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaiementBanqueDTO} or a {@link Page} of {@link PaiementBanqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaiementBanqueQueryService extends QueryService<PaiementBanque> {

    private final Logger log = LoggerFactory.getLogger(PaiementBanqueQueryService.class);

    private final PaiementBanqueRepository paiementBanqueRepository;

    private final PaiementBanqueMapper paiementBanqueMapper;

    public PaiementBanqueQueryService(PaiementBanqueRepository paiementBanqueRepository, PaiementBanqueMapper paiementBanqueMapper) {
        this.paiementBanqueRepository = paiementBanqueRepository;
        this.paiementBanqueMapper = paiementBanqueMapper;
    }

    /**
     * Return a {@link List} of {@link PaiementBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementBanqueDTO> findByCriteria(PaiementBanqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaiementBanque> specification = createSpecification(criteria);
        return paiementBanqueMapper.toDto(paiementBanqueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaiementBanqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementBanqueDTO> findByCriteria(PaiementBanqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaiementBanque> specification = createSpecification(criteria);
        return paiementBanqueRepository.findAll(specification, page).map(paiementBanqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaiementBanqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaiementBanque> specification = createSpecification(criteria);
        return paiementBanqueRepository.count(specification);
    }

    /**
     * Function to convert {@link PaiementBanqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaiementBanque> createSpecification(PaiementBanqueCriteria criteria) {
        Specification<PaiementBanque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaiementBanque_.id));
            }
            if (criteria.getReferencePaiement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReferencePaiement(), PaiementBanque_.referencePaiement));
            }
        }
        return specification;
    }
}
