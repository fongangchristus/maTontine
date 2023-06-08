package com.it4innov.repository;

import com.it4innov.domain.Adhesion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Adhesion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdhesionRepository extends JpaRepository<Adhesion, Long>, JpaSpecificationExecutor<Adhesion> {}
