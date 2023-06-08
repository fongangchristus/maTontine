package com.it4innov.service.mapper;

import com.it4innov.domain.Pot;
import com.it4innov.domain.TypePot;
import com.it4innov.service.dto.PotDTO;
import com.it4innov.service.dto.TypePotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pot} and its DTO {@link PotDTO}.
 */
@Mapper(componentModel = "spring")
public interface PotMapper extends EntityMapper<PotDTO, Pot> {
    @Mapping(target = "typePot", source = "typePot", qualifiedByName = "typePotId")
    PotDTO toDto(Pot s);

    @Named("typePotId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypePotDTO toDtoTypePotId(TypePot typePot);
}
