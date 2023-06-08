package com.it4innov.repository;

import com.it4innov.domain.GestionnaireTontine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GestionnaireTontine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GestionnaireTontineRepository
    extends JpaRepository<GestionnaireTontine, Long>, JpaSpecificationExecutor<GestionnaireTontine> {}
