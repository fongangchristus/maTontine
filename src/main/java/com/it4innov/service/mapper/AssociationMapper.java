package com.it4innov.service.mapper;

import com.it4innov.domain.Association;
import com.it4innov.domain.Monnaie;
import com.it4innov.service.dto.AssociationDTO;
import com.it4innov.service.dto.MonnaieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Association} and its DTO {@link AssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssociationMapper extends EntityMapper<AssociationDTO, Association> {
    @Mapping(target = "monnaie", source = "monnaie", qualifiedByName = "monnaieId")
    AssociationDTO toDto(Association s);

    @Named("monnaieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MonnaieDTO toDtoMonnaieId(Monnaie monnaie);
}
