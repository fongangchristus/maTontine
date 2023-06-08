package com.it4innov.repository;

import com.it4innov.domain.GestionnaireBanque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GestionnaireBanque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GestionnaireBanqueRepository
    extends JpaRepository<GestionnaireBanque, Long>, JpaSpecificationExecutor<GestionnaireBanque> {}
