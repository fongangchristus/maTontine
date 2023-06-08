package com.it4innov.service.mapper;

import com.it4innov.domain.Adhesion;
import com.it4innov.domain.PaiementAdhesion;
import com.it4innov.service.dto.AdhesionDTO;
import com.it4innov.service.dto.PaiementAdhesionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaiementAdhesion} and its DTO {@link PaiementAdhesionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementAdhesionMapper extends EntityMapper<PaiementAdhesionDTO, PaiementAdhesion> {
    @Mapping(target = "adhesion", source = "adhesion", qualifiedByName = "adhesionId")
    PaiementAdhesionDTO toDto(PaiementAdhesion s);

    @Named("adhesionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdhesionDTO toDtoAdhesionId(Adhesion adhesion);
}
