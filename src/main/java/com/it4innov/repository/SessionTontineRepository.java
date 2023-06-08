package com.it4innov.repository;

import com.it4innov.domain.SessionTontine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SessionTontine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionTontineRepository extends JpaRepository<SessionTontine, Long>, JpaSpecificationExecutor<SessionTontine> {}
