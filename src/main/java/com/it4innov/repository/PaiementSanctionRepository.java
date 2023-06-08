package com.it4innov.repository;

import com.it4innov.domain.PaiementSanction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaiementSanction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementSanctionRepository extends JpaRepository<PaiementSanction, Long>, JpaSpecificationExecutor<PaiementSanction> {}
