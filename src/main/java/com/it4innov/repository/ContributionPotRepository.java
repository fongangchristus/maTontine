package com.it4innov.repository;

import com.it4innov.domain.ContributionPot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContributionPot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContributionPotRepository extends JpaRepository<ContributionPot, Long>, JpaSpecificationExecutor<ContributionPot> {}
