package com.it4innov.repository;

import com.it4innov.domain.TypeEvenement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeEvenement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeEvenementRepository extends JpaRepository<TypeEvenement, Long>, JpaSpecificationExecutor<TypeEvenement> {}
