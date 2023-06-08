package com.it4innov.repository;

import com.it4innov.domain.Banque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Banque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BanqueRepository extends JpaRepository<Banque, Long>, JpaSpecificationExecutor<Banque> {}
