package com.it4innov.repository;

import com.it4innov.domain.HistoriquePersonne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoriquePersonne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriquePersonneRepository
    extends JpaRepository<HistoriquePersonne, Long>, JpaSpecificationExecutor<HistoriquePersonne> {}
