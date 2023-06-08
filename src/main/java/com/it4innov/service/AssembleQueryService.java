package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Assemble;
import com.it4innov.repository.AssembleRepository;
import com.it4innov.service.criteria.AssembleCriteria;
import com.it4innov.service.dto.AssembleDTO;
import com.it4innov.service.mapper.AssembleMapper;
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
 * Service for executing complex queries for {@link Assemble} entities in the database.
 * The main input is a {@link AssembleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssembleDTO} or a {@link Page} of {@link AssembleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssembleQueryService extends QueryService<Assemble> {

    private final Logger log = LoggerFactory.getLogger(AssembleQueryService.class);

    private final AssembleRepository assembleRepository;

    private final AssembleMapper assembleMapper;

    public AssembleQueryService(AssembleRepository assembleRepository, AssembleMapper assembleMapper) {
        this.assembleRepository = assembleRepository;
        this.assembleMapper = assembleMapper;
    }

    /**
     * Return a {@link List} of {@link AssembleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssembleDTO> findByCriteria(AssembleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Assemble> specification = createSpecification(criteria);
        return assembleMapper.toDto(assembleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssembleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssembleDTO> findByCriteria(AssembleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Assemble> specification = createSpecification(criteria);
        return assembleRepository.findAll(specification, page).map(assembleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssembleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Assemble> specification = createSpecification(criteria);
        return assembleRepository.count(specification);
    }

    /**
     * Function to convert {@link AssembleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Assemble> createSpecification(AssembleCriteria criteria) {
        Specification<Assemble> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Assemble_.id));
            }
            if (criteria.getCodeAssociation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeAssociation(), Assemble_.codeAssociation));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Assemble_.libele));
            }
            if (criteria.getEnLigne() != null) {
                specification = specification.and(buildSpecification(criteria.getEnLigne(), Assemble_.enLigne));
            }
            if (criteria.getDateSeance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDateSeance(), Assemble_.dateSeance));
            }
            if (criteria.getLieuSeance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLieuSeance(), Assemble_.lieuSeance));
            }
            if (criteria.getMatriculeMembreRecoit() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMatriculeMembreRecoit(), Assemble_.matriculeMembreRecoit));
            }
            if (criteria.getNature() != null) {
                specification = specification.and(buildSpecification(criteria.getNature(), Assemble_.nature));
            }
            if (criteria.getCompteRendu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompteRendu(), Assemble_.compteRendu));
            }
            if (criteria.getResumeAssemble() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResumeAssemble(), Assemble_.resumeAssemble));
            }
            if (criteria.getDocumentCRPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentCRPath(), Assemble_.documentCRPath));
            }
        }
        return specification;
    }
}
