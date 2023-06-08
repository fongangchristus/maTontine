package com.it4innov.repository;

import com.it4innov.domain.Monnaie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Monnaie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonnaieRepository extends JpaRepository<Monnaie, Long>, JpaSpecificationExecutor<Monnaie> {}
