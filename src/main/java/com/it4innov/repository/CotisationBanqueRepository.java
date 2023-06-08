package com.it4innov.repository;

import com.it4innov.domain.CotisationBanque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CotisationBanque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CotisationBanqueRepository extends JpaRepository<CotisationBanque, Long>, JpaSpecificationExecutor<CotisationBanque> {}
