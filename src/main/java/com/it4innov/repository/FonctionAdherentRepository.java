package com.it4innov.repository;

import com.it4innov.domain.FonctionAdherent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FonctionAdherent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FonctionAdherentRepository extends JpaRepository<FonctionAdherent, Long>, JpaSpecificationExecutor<FonctionAdherent> {}
