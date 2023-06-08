package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Adresse;
import com.it4innov.repository.AdresseRepository;
import com.it4innov.service.criteria.AdresseCriteria;
import com.it4innov.service.dto.AdresseDTO;
import com.it4innov.service.mapper.AdresseMapper;
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
 * Service for executing complex queries for {@link Adresse} entities in the database.
 * The main input is a {@link AdresseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdresseDTO} or a {@link Page} of {@link AdresseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdresseQueryService extends QueryService<Adresse> {

    private final Logger log = LoggerFactory.getLogger(AdresseQueryService.class);

    private final AdresseRepository adresseRepository;

    private final AdresseMapper adresseMapper;

    public AdresseQueryService(AdresseRepository adresseRepository, AdresseMapper adresseMapper) {
        this.adresseRepository = adresseRepository;
        this.adresseMapper = adresseMapper;
    }

    /**
     * Return a {@link List} of {@link AdresseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdresseDTO> findByCriteria(AdresseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Adresse> specification = createSpecification(criteria);
        return adresseMapper.toDto(adresseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdresseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdresseDTO> findByCriteria(AdresseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Adresse> specification = createSpecification(criteria);
        return adresseRepository.findAll(specification, page).map(adresseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdresseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Adresse> specification = createSpecification(criteria);
        return adresseRepository.count(specification);
    }

    /**
     * Function to convert {@link AdresseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Adresse> createSpecification(AdresseCriteria criteria) {
        Specification<Adresse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Adresse_.id));
            }
            if (criteria.getDepartmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepartmentName(), Adresse_.departmentName));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Adresse_.streetAddress));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Adresse_.postalCode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Adresse_.city));
            }
            if (criteria.getStateProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateProvince(), Adresse_.stateProvince));
            }
            if (criteria.getPays() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPays(), Adresse_.pays));
            }
        }
        return specification;
    }
}
