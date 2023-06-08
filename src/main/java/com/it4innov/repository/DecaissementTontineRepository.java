package com.it4innov.repository;

import com.it4innov.domain.DecaissementTontine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DecaissementTontine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecaissementTontineRepository
    extends JpaRepository<DecaissementTontine, Long>, JpaSpecificationExecutor<DecaissementTontine> {}
