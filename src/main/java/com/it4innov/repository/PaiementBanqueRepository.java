package com.it4innov.repository;

import com.it4innov.domain.PaiementBanque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaiementBanque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementBanqueRepository extends JpaRepository<PaiementBanque, Long>, JpaSpecificationExecutor<PaiementBanque> {}
