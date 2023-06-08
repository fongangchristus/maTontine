package com.it4innov.repository;

import com.it4innov.domain.Sanction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sanction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long>, JpaSpecificationExecutor<Sanction> {}
