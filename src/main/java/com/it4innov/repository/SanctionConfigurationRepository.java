package com.it4innov.repository;

import com.it4innov.domain.SanctionConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SanctionConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SanctionConfigurationRepository
    extends JpaRepository<SanctionConfiguration, Long>, JpaSpecificationExecutor<SanctionConfiguration> {}
