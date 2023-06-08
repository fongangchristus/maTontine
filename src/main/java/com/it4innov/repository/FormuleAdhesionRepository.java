package com.it4innov.repository;

import com.it4innov.domain.FormuleAdhesion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FormuleAdhesion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormuleAdhesionRepository extends JpaRepository<FormuleAdhesion, Long>, JpaSpecificationExecutor<FormuleAdhesion> {}
