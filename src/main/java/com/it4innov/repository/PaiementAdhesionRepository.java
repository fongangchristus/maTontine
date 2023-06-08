package com.it4innov.repository;

import com.it4innov.domain.PaiementAdhesion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaiementAdhesion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementAdhesionRepository extends JpaRepository<PaiementAdhesion, Long>, JpaSpecificationExecutor<PaiementAdhesion> {}
