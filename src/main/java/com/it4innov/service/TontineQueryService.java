package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Tontine;
import com.it4innov.repository.TontineRepository;
import com.it4innov.service.criteria.TontineCriteria;
import com.it4innov.service.dto.TontineDTO;
import com.it4innov.service.mapper.TontineMapper;
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
 * Service for executing complex queries for {@link Tontine} entities in the database.
 * The main input is a {@link TontineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TontineDTO} or a {@link Page} of {@link TontineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TontineQueryService extends QueryService<Tontine> {

    private final Logger log = LoggerFactory.getLogger(TontineQueryService.class);

    private final TontineRepository tontineRepository;

    private final TontineMapper tontineMapper;

    public TontineQueryService(TontineRepository tontineRepository, TontineMapper tontineMapper) {
        this.tontineRepository = tontineRepository;
        this.tontineMapper = tontineMapper;
    }

    /**
     * Return a {@link List} of {@link TontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TontineDTO> findByCriteria(TontineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tontine> specification = createSpecification(criteria);
        return tontineMapper.toDto(tontineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TontineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TontineDTO> findByCriteria(TontineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tontine> specification = createSpecification(criteria);
        return tontineRepository.findAll(specification, page).map(tontineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TontineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tontine> specification = createSpecification(criteria);
        return tontineRepository.count(specification);
    }

    /**
     * Function to convert {@link TontineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tontine> createSpecification(TontineCriteria criteria) {
        Specification<Tontine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tontine_.id));
            }
            if (criteria.getCodeAssociation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeAssociation(), Tontine_.codeAssociation));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Tontine_.libele));
            }
            if (criteria.getNombreTour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNombreTour(), Tontine_.nombreTour));
            }
            if (criteria.getNombrePersonne() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNombrePersonne(), Tontine_.nombrePersonne));
            }
            if (criteria.getMargeBeneficiaire() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMargeBeneficiaire(), Tontine_.margeBeneficiaire));
            }
            if (criteria.getMontantPart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantPart(), Tontine_.montantPart));
            }
            if (criteria.getMontantCagnote() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantCagnote(), Tontine_.montantCagnote));
            }
            if (criteria.getPenaliteRetardCotisation() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPenaliteRetardCotisation(), Tontine_.penaliteRetardCotisation));
            }
            if (criteria.getTypePenalite() != null) {
                specification = specification.and(buildSpecification(criteria.getTypePenalite(), Tontine_.typePenalite));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Tontine_.dateCreation));
            }
            if (criteria.getDatePremierTour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePremierTour(), Tontine_.datePremierTour));
            }
            if (criteria.getDateDernierTour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDernierTour(), Tontine_.dateDernierTour));
            }
            if (criteria.getStatutTontine() != null) {
                specification = specification.and(buildSpecification(criteria.getStatutTontine(), Tontine_.statutTontine));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Tontine_.description));
            }
            if (criteria.getSessionTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSessionTontineId(),
                            root -> root.join(Tontine_.sessionTontines, JoinType.LEFT).get(SessionTontine_.id)
                        )
                    );
            }
            if (criteria.getGestionnaireTontineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGestionnaireTontineId(),
                            root -> root.join(Tontine_.gestionnaireTontines, JoinType.LEFT).get(GestionnaireTontine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
