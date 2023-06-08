package com.it4innov.repository;

import com.it4innov.domain.CompteTontine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompteTontine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteTontineRepository extends JpaRepository<CompteTontine, Long>, JpaSpecificationExecutor<CompteTontine> {}
