package com.it4innov.repository;

import com.it4innov.domain.CompteRIB;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompteRIB entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteRIBRepository extends JpaRepository<CompteRIB, Long>, JpaSpecificationExecutor<CompteRIB> {}
