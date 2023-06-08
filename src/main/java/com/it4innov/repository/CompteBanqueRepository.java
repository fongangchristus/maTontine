package com.it4innov.repository;

import com.it4innov.domain.CompteBanque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompteBanque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteBanqueRepository extends JpaRepository<CompteBanque, Long>, JpaSpecificationExecutor<CompteBanque> {}
