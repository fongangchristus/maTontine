package com.it4innov.service.mapper;

import com.it4innov.domain.SanctionConfiguration;
import com.it4innov.service.dto.SanctionConfigurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SanctionConfiguration} and its DTO {@link SanctionConfigurationDTO}.
 */
@Mapper(componentModel = "spring")
public interface SanctionConfigurationMapper extends EntityMapper<SanctionConfigurationDTO, SanctionConfiguration> {}
