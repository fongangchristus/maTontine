package com.it4innov.service.mapper;

import com.it4innov.domain.PaiementSanction;
import com.it4innov.domain.Sanction;
import com.it4innov.service.dto.PaiementSanctionDTO;
import com.it4innov.service.dto.SanctionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaiementSanction} and its DTO {@link PaiementSanctionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementSanctionMapper extends EntityMapper<PaiementSanctionDTO, PaiementSanction> {
    @Mapping(target = "sanction", source = "sanction", qualifiedByName = "sanctionId")
    PaiementSanctionDTO toDto(PaiementSanction s);

    @Named("sanctionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SanctionDTO toDtoSanctionId(Sanction sanction);
}
