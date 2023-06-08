package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.CommentairePot;
import com.it4innov.repository.CommentairePotRepository;
import com.it4innov.service.criteria.CommentairePotCriteria;
import com.it4innov.service.dto.CommentairePotDTO;
import com.it4innov.service.mapper.CommentairePotMapper;
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
 * Service for executing complex queries for {@link CommentairePot} entities in the database.
 * The main input is a {@link CommentairePotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentairePotDTO} or a {@link Page} of {@link CommentairePotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentairePotQueryService extends QueryService<CommentairePot> {

    private final Logger log = LoggerFactory.getLogger(CommentairePotQueryService.class);

    private final CommentairePotRepository commentairePotRepository;

    private final CommentairePotMapper commentairePotMapper;

    public CommentairePotQueryService(CommentairePotRepository commentairePotRepository, CommentairePotMapper commentairePotMapper) {
        this.commentairePotRepository = commentairePotRepository;
        this.commentairePotMapper = commentairePotMapper;
    }

    /**
     * Return a {@link List} of {@link CommentairePotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentairePotDTO> findByCriteria(CommentairePotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommentairePot> specification = createSpecification(criteria);
        return commentairePotMapper.toDto(commentairePotRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentairePotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentairePotDTO> findByCriteria(CommentairePotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommentairePot> specification = createSpecification(criteria);
        return commentairePotRepository.findAll(specification, page).map(commentairePotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentairePotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommentairePot> specification = createSpecification(criteria);
        return commentairePotRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentairePotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommentairePot> createSpecification(CommentairePotCriteria criteria) {
        Specification<CommentairePot> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommentairePot_.id));
            }
            if (criteria.getMatriculeContributeur() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMatriculeContributeur(), CommentairePot_.matriculeContributeur));
            }
            if (criteria.getIdentifiantPot() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentifiantPot(), CommentairePot_.identifiantPot));
            }
            if (criteria.getContenu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContenu(), CommentairePot_.contenu));
            }
            if (criteria.getDateComentaire() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateComentaire(), CommentairePot_.dateComentaire));
            }
            if (criteria.getPotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPotId(), root -> root.join(CommentairePot_.pot, JoinType.LEFT).get(Pot_.id))
                    );
            }
        }
        return specification;
    }
}
