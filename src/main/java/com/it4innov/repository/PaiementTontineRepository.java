package com.it4innov.repository;

import com.it4innov.domain.PaiementTontine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaiementTontine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementTontineRepository extends JpaRepository<PaiementTontine, Long>, JpaSpecificationExecutor<PaiementTontine> {}
