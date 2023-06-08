package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Exercise;
import com.it4innov.repository.ExerciseRepository;
import com.it4innov.service.criteria.ExerciseCriteria;
import com.it4innov.service.dto.ExerciseDTO;
import com.it4innov.service.mapper.ExerciseMapper;
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
 * Service for executing complex queries for {@link Exercise} entities in the database.
 * The main input is a {@link ExerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExerciseDTO} or a {@link Page} of {@link ExerciseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseQueryService extends QueryService<Exercise> {

    private final Logger log = LoggerFactory.getLogger(ExerciseQueryService.class);

    private final ExerciseRepository exerciseRepository;

    private final ExerciseMapper exerciseMapper;

    public ExerciseQueryService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    /**
     * Return a {@link List} of {@link ExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciseDTO> findByCriteria(ExerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseMapper.toDto(exerciseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseDTO> findByCriteria(ExerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseRepository.findAll(specification, page).map(exerciseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Exercise> createSpecification(ExerciseCriteria criteria) {
        Specification<Exercise> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Exercise_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Exercise_.libele));
            }
            if (criteria.getObservation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservation(), Exercise_.observation));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Exercise_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Exercise_.dateFin));
            }
            if (criteria.getStatut() != null) {
                specification = specification.and(buildSpecification(criteria.getStatut(), Exercise_.statut));
            }
            if (criteria.getAssociationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssociationId(),
                            root -> root.join(Exercise_.association, JoinType.LEFT).get(Association_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
