package com.it4innov.repository;

import com.it4innov.domain.TypePot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypePot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePotRepository extends JpaRepository<TypePot, Long>, JpaSpecificationExecutor<TypePot> {}
