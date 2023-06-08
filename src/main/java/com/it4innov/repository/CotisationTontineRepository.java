package com.it4innov.repository;

import com.it4innov.domain.CotisationTontine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CotisationTontine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CotisationTontineRepository extends JpaRepository<CotisationTontine, Long>, JpaSpecificationExecutor<CotisationTontine> {}
