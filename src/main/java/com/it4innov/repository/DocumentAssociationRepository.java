package com.it4innov.repository;

import com.it4innov.domain.DocumentAssociation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentAssociation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentAssociationRepository
    extends JpaRepository<DocumentAssociation, Long>, JpaSpecificationExecutor<DocumentAssociation> {}
