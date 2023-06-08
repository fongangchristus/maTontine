package com.it4innov.repository;

import com.it4innov.domain.DecaisementBanque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DecaisementBanque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecaisementBanqueRepository extends JpaRepository<DecaisementBanque, Long>, JpaSpecificationExecutor<DecaisementBanque> {}
