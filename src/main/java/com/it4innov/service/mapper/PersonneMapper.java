package com.it4innov.service.mapper;

import com.it4innov.domain.Adresse;
import com.it4innov.domain.Personne;
import com.it4innov.service.dto.AdresseDTO;
import com.it4innov.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personne} and its DTO {@link PersonneDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonneMapper extends EntityMapper<PersonneDTO, Personne> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "adresseId")
    PersonneDTO toDto(Personne s);

    @Named("adresseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdresseDTO toDtoAdresseId(Adresse adresse);
}
