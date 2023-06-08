package com.it4innov.repository;

import com.it4innov.domain.Tontine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tontine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TontineRepository extends JpaRepository<Tontine, Long>, JpaSpecificationExecutor<Tontine> {}
