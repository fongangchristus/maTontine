package com.it4innov.repository;

import com.it4innov.domain.Pot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PotRepository extends JpaRepository<Pot, Long>, JpaSpecificationExecutor<Pot> {}
