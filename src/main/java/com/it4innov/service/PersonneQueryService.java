package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Personne;
import com.it4innov.repository.PersonneRepository;
import com.it4innov.service.criteria.PersonneCriteria;
import com.it4innov.service.dto.PersonneDTO;
import com.it4innov.service.mapper.PersonneMapper;
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
 * Service for executing complex queries for {@link Personne} entities in the database.
 * The main input is a {@link PersonneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonneDTO} or a {@link Page} of {@link PersonneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonneQueryService extends QueryService<Personne> {

    private final Logger log = LoggerFactory.getLogger(PersonneQueryService.class);

    private final PersonneRepository personneRepository;

    private final PersonneMapper personneMapper;

    public PersonneQueryService(PersonneRepository personneRepository, PersonneMapper personneMapper) {
        this.personneRepository = personneRepository;
        this.personneMapper = personneMapper;
    }

    /**
     * Return a {@link List} of {@link PersonneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonneDTO> findByCriteria(PersonneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Personne> specification = createSpecification(criteria);
        return personneMapper.toDto(personneRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonneDTO> findByCriteria(PersonneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Personne> specification = createSpecification(criteria);
        return personneRepository.findAll(specification, page).map(personneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Personne> specification = createSpecification(criteria);
        return personneRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Personne> createSpecification(PersonneCriteria criteria) {
        Specification<Personne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Personne_.id));
            }
            if (criteria.getIdUser() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdUser(), Personne_.idUser));
            }
            if (criteria.getCodeAssociation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodeAssociation(), Personne_.codeAssociation));
            }
            if (criteria.getMatricule() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatricule(), Personne_.matricule));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Personne_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Personne_.prenom));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Personne_.telephone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Personne_.email));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Personne_.dateNaissance));
            }
            if (criteria.getLieuNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLieuNaissance(), Personne_.lieuNaissance));
            }
            if (criteria.getDateInscription() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateInscription(), Personne_.dateInscription));
            }
            if (criteria.getProfession() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfession(), Personne_.profession));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildSpecification(criteria.getSexe(), Personne_.sexe));
            }
            if (criteria.getPhotoPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhotoPath(), Personne_.photoPath));
            }
            if (criteria.getDateIntegration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateIntegration(), Personne_.dateIntegration));
            }
            if (criteria.getIsAdmin() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAdmin(), Personne_.isAdmin));
            }
            if (criteria.getIsDonateur() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDonateur(), Personne_.isDonateur));
            }
            if (criteria.getIsBenevole() != null) {
                specification = specification.and(buildSpecification(criteria.getIsBenevole(), Personne_.isBenevole));
            }
            if (criteria.getTypePersonne() != null) {
                specification = specification.and(buildSpecification(criteria.getTypePersonne(), Personne_.typePersonne));
            }
            if (criteria.getAdresseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAdresseId(), root -> root.join(Personne_.adresse, JoinType.LEFT).get(Adresse_.id))
                    );
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getContactId(), root -> root.join(Personne_.contacts, JoinType.LEFT).get(Contact_.id))
                    );
            }
            if (criteria.getCompteRIBId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompteRIBId(),
                            root -> root.join(Personne_.compteRIBS, JoinType.LEFT).get(CompteRIB_.id)
                        )
                    );
            }
            if (criteria.getHistoriquePersonneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHistoriquePersonneId(),
                            root -> root.join(Personne_.historiquePersonnes, JoinType.LEFT).get(HistoriquePersonne_.id)
                        )
                    );
            }
            if (criteria.getPresenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPresenceId(),
                            root -> root.join(Personne_.presences, JoinType.LEFT).get(Presence_.id)
                        )
                    );
            }
            if (criteria.getFonctionAdherentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFonctionAdherentId(),
                            root -> root.join(Personne_.fonctionAdherents, JoinType.LEFT).get(FonctionAdherent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
