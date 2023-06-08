package com.it4innov.repository;

import com.it4innov.domain.Fonction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fonction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FonctionRepository extends JpaRepository<Fonction, Long>, JpaSpecificationExecutor<Fonction> {}
