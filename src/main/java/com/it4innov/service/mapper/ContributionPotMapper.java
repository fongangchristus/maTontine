package com.it4innov.service.mapper;

import com.it4innov.domain.ContributionPot;
import com.it4innov.domain.Pot;
import com.it4innov.service.dto.ContributionPotDTO;
import com.it4innov.service.dto.PotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContributionPot} and its DTO {@link ContributionPotDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContributionPotMapper extends EntityMapper<ContributionPotDTO, ContributionPot> {
    @Mapping(target = "pot", source = "pot", qualifiedByName = "potId")
    ContributionPotDTO toDto(ContributionPot s);

    @Named("potId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PotDTO toDtoPotId(Pot pot);
}
