package com.it4innov.service.mapper;

import com.it4innov.domain.Evenement;
import com.it4innov.domain.TypeEvenement;
import com.it4innov.service.dto.EvenementDTO;
import com.it4innov.service.dto.TypeEvenementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evenement} and its DTO {@link EvenementDTO}.
 */
@Mapper(componentModel = "spring")
public interface EvenementMapper extends EntityMapper<EvenementDTO, Evenement> {
    @Mapping(target = "typeEvenement", source = "typeEvenement", qualifiedByName = "typeEvenementId")
    EvenementDTO toDto(Evenement s);

    @Named("typeEvenementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeEvenementDTO toDtoTypeEvenementId(TypeEvenement typeEvenement);
}
