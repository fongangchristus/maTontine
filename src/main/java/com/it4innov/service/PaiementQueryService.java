package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Paiement;
import com.it4innov.repository.PaiementRepository;
import com.it4innov.service.criteria.PaiementCriteria;
import com.it4innov.service.dto.PaiementDTO;
import com.it4innov.service.mapper.PaiementMapper;
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
 * Service for executing complex queries for {@link Paiement} entities in the database.
 * The main input is a {@link PaiementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaiementDTO} or a {@link Page} of {@link PaiementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaiementQueryService extends QueryService<Paiement> {

    private final Logger log = LoggerFactory.getLogger(PaiementQueryService.class);

    private final PaiementRepository paiementRepository;

    private final PaiementMapper paiementMapper;

    public PaiementQueryService(PaiementRepository paiementRepository, PaiementMapper paiementMapper) {
        this.paiementRepository = paiementRepository;
        this.paiementMapper = paiementMapper;
    }

    /**
     * Return a {@link List} of {@link PaiementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> findByCriteria(PaiementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Paiement> specification = createSpecification(criteria);
        return paiementMapper.toDto(paiementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaiementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementDTO> findByCriteria(PaiementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Paiement> specification = createSpecification(criteria);
        return paiementRepository.findAll(specification, page).map(paiementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaiementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Paiement> specification = createSpecification(criteria);
        return paiementRepository.count(specification);
    }

    /**
     * Function to convert {@link PaiementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Paiement> createSpecification(PaiementCriteria criteria) {
        Specification<Paiement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Paiement_.id));
            }
            if (criteria.getCodeAssociation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeAssociation(), Paiement_.codeAssociation));
            }
            if (criteria.getReferencePaiement() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferencePaiement(), Paiement_.referencePaiement));
            }
            if (criteria.getMatriculecmptEmet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatriculecmptEmet(), Paiement_.matriculecmptEmet));
            }
            if (criteria.getMatriculecmptDest() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatriculecmptDest(), Paiement_.matriculecmptDest));
            }
            if (criteria.getMontantPaiement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantPaiement(), Paiement_.montantPaiement));
            }
            if (criteria.getDatePaiement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePaiement(), Paiement_.datePaiement));
            }
            if (criteria.getModePaiement() != null) {
                specification = specification.and(buildSpecification(criteria.getModePaiement(), Paiement_.modePaiement));
            }
            if (criteria.getStatutPaiement() != null) {
                specification = specification.and(buildSpecification(criteria.getStatutPaiement(), Paiement_.statutPaiement));
            }
        }
        return specification;
    }
}
