package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.DocumentAssociation;
import com.it4innov.repository.DocumentAssociationRepository;
import com.it4innov.service.criteria.DocumentAssociationCriteria;
import com.it4innov.service.dto.DocumentAssociationDTO;
import com.it4innov.service.mapper.DocumentAssociationMapper;
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
 * Service for executing complex queries for {@link DocumentAssociation} entities in the database.
 * The main input is a {@link DocumentAssociationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentAssociationDTO} or a {@link Page} of {@link DocumentAssociationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentAssociationQueryService extends QueryService<DocumentAssociation> {

    private final Logger log = LoggerFactory.getLogger(DocumentAssociationQueryService.class);

    private final DocumentAssociationRepository documentAssociationRepository;

    private final DocumentAssociationMapper documentAssociationMapper;

    public DocumentAssociationQueryService(
        DocumentAssociationRepository documentAssociationRepository,
        DocumentAssociationMapper documentAssociationMapper
    ) {
        this.documentAssociationRepository = documentAssociationRepository;
        this.documentAssociationMapper = documentAssociationMapper;
    }

    /**
     * Return a {@link List} of {@link DocumentAssociationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentAssociationDTO> findByCriteria(DocumentAssociationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocumentAssociation> specification = createSpecification(criteria);
        return documentAssociationMapper.toDto(documentAssociationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocumentAssociationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentAssociationDTO> findByCriteria(DocumentAssociationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentAssociation> specification = createSpecification(criteria);
        return documentAssociationRepository.findAll(specification, page).map(documentAssociationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentAssociationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocumentAssociation> specification = createSpecification(criteria);
        return documentAssociationRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentAssociationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentAssociation> createSpecification(DocumentAssociationCriteria criteria) {
        Specification<DocumentAssociation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocumentAssociation_.id));
            }
            if (criteria.getCodeDocument() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeDocument(), DocumentAssociation_.codeDocument));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), DocumentAssociation_.libele));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DocumentAssociation_.description));
            }
            if (criteria.getDateEnregistrement() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDateEnregistrement(), DocumentAssociation_.dateEnregistrement));
            }
            if (criteria.getDateArchivage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateArchivage(), DocumentAssociation_.dateArchivage));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), DocumentAssociation_.version));
            }
            if (criteria.getAssociationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssociationId(),
                            root -> root.join(DocumentAssociation_.association, JoinType.LEFT).get(Association_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
