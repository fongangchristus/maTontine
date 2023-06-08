package com.it4innov.repository;

import com.it4innov.domain.Assemble;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Assemble entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssembleRepository extends JpaRepository<Assemble, Long>, JpaSpecificationExecutor<Assemble> {}
