package com.it4innov.repository;

import com.it4innov.domain.CommentairePot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommentairePot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentairePotRepository extends JpaRepository<CommentairePot, Long>, JpaSpecificationExecutor<CommentairePot> {}
