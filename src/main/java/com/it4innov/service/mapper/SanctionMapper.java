package com.it4innov.service.mapper;

import com.it4innov.domain.Sanction;
import com.it4innov.domain.SanctionConfiguration;
import com.it4innov.service.dto.SanctionConfigurationDTO;
import com.it4innov.service.dto.SanctionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sanction} and its DTO {@link SanctionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SanctionMapper extends EntityMapper<SanctionDTO, Sanction> {
    @Mapping(target = "sanctionConfig", source = "sanctionConfig", qualifiedByName = "sanctionConfigurationId")
    SanctionDTO toDto(Sanction s);

    @Named("sanctionConfigurationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SanctionConfigurationDTO toDtoSanctionConfigurationId(SanctionConfiguration sanctionConfiguration);
}
