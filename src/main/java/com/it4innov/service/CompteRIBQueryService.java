package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.CompteRIB;
import com.it4innov.repository.CompteRIBRepository;
import com.it4innov.service.criteria.CompteRIBCriteria;
import com.it4innov.service.dto.CompteRIBDTO;
import com.it4innov.service.mapper.CompteRIBMapper;
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
 * Service for executing complex queries for {@link CompteRIB} entities in the database.
 * The main input is a {@link CompteRIBCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompteRIBDTO} or a {@link Page} of {@link CompteRIBDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompteRIBQueryService extends QueryService<CompteRIB> {

    private final Logger log = LoggerFactory.getLogger(CompteRIBQueryService.class);

    private final CompteRIBRepository compteRIBRepository;

    private final CompteRIBMapper compteRIBMapper;

    public CompteRIBQueryService(CompteRIBRepository compteRIBRepository, CompteRIBMapper compteRIBMapper) {
        this.compteRIBRepository = compteRIBRepository;
        this.compteRIBMapper = compteRIBMapper;
    }

    /**
     * Return a {@link List} of {@link CompteRIBDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompteRIBDTO> findByCriteria(CompteRIBCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CompteRIB> specification = createSpecification(criteria);
        return compteRIBMapper.toDto(compteRIBRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompteRIBDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompteRIBDTO> findByCriteria(CompteRIBCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CompteRIB> specification = createSpecification(criteria);
        return compteRIBRepository.findAll(specification, page).map(compteRIBMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompteRIBCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CompteRIB> specification = createSpecification(criteria);
        return compteRIBRepository.count(specification);
    }

    /**
     * Function to convert {@link CompteRIBCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CompteRIB> createSpecification(CompteRIBCriteria criteria) {
        Specification<CompteRIB> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CompteRIB_.id));
            }
            if (criteria.getIban() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIban(), CompteRIB_.iban));
            }
            if (criteria.getTitulaireCompte() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulaireCompte(), CompteRIB_.titulaireCompte));
            }
            if (criteria.getVerifier() != null) {
                specification = specification.and(buildSpecification(criteria.getVerifier(), CompteRIB_.verifier));
            }
            if (criteria.getAdherentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAdherentId(),
                            root -> root.join(CompteRIB_.adherent, JoinType.LEFT).get(Personne_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
