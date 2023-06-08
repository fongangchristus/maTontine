package com.it4innov.service.mapper;

import com.it4innov.domain.CompteRIB;
import com.it4innov.domain.Personne;
import com.it4innov.service.dto.CompteRIBDTO;
import com.it4innov.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompteRIB} and its DTO {@link CompteRIBDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteRIBMapper extends EntityMapper<CompteRIBDTO, CompteRIB> {
    @Mapping(target = "adherent", source = "adherent", qualifiedByName = "personneId")
    CompteRIBDTO toDto(CompteRIB s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}
