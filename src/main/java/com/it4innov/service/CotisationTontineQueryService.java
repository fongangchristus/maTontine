package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.CotisationTontine;
import com.it4innov.repository.CotisationTontineRepository;
import com.it4innov.service.criteria.CotisationTontineCriteria;
import com.it4innov.service.dto.CotisationTontineDTO;
import com.it4innov.service.mapper.CotisationTontineMapper;
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
 * Service for executing complex queries for {@link CotisationTontine} entities in the database.
 * The main input is a {@link CotisationTontineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CotisationTontineDTO} or a {@link Page} of {@link CotisationTontineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CotisationTontineQueryService extends QueryService<CotisationTontine> {

    private final Logger log = LoggerFactory.getLogger(CotisationTontineQueryService.class);

    private final CotisationTontineRepository cotisationTontineRepository;

    private final CotisationTontineMapper cotisationTontineMapper;

    public CotisationTontineQueryService(
        CotisationTontineRepository cotisationTontineRepository,
        CotisationTontineMapper cotisationTontineMapper
    ) {
        this.cotisationTontineRepository = cotisationTontineRepository;
        this.cotisationTontineMapper = cotisationTontineMapper;
    }

    /**
     * Return a {@link List} of {@link CotisationTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CotisationTontineDTO> findByCriteria(CotisationTontineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CotisationTontine> specification = createSpecification(criteria);
        return cotisationTontineMapper.toDto(cotisationTontineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CotisationTontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CotisationTontineDTO> findByCriteria(CotisationTontineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CotisationTontine> specification = createSpecification(criteria);
        return cotisationTontineRepository.findAll(specification, page).map(cotisationTontineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CotisationTontineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CotisationTontine> specification = createSpecification(criteria);
        return cotisationTontineRepository.count(specification);
    }

    /**
     * Function to convert {@link CotisationTontineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CotisationTontine> createSpecification(CotisationTontineCriteria criteria) {
        Specification<CotisationTontine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CotisationTontine_.id));
            }
            if (criteria.getMontantCotise() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantCotise(), CotisationTontine_.montantCotise));
            }
            if (criteria.getPieceJustifPath() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPieceJustifPath(), CotisationTontine_.pieceJustifPath));
            }
            if (criteria.getDateCotisation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCotisation(), CotisationTontine_.dateCotisation));
            }
            if (criteria.getDateValidation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateValidation(), CotisationTontine_.dateValidation));
            }
            if (criteria.getCommentaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommentaire(), CotisationTontine_.commentaire));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildSpecification(criteria.getEtat(), CotisationTontine_.etat));
            }
            if (criteria.getPaiementTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaiementTontineId(),
                            root -> root.join(CotisationTontine_.paiementTontines, JoinType.LEFT).get(PaiementTontine_.id)
                        )
                    );
            }
            if (criteria.getSessionTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSessionTontineId(),
                            root -> root.join(CotisationTontine_.sessionTontine, JoinType.LEFT).get(SessionTontine_.id)
                        )
                    );
            }
            if (criteria.getCompteTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompteTontineId(),
                            root -> root.join(CotisationTontine_.compteTontine, JoinType.LEFT).get(CompteTontine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
