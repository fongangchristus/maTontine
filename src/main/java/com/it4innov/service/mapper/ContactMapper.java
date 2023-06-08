package com.it4innov.service.mapper;

import com.it4innov.domain.Contact;
import com.it4innov.domain.Personne;
import com.it4innov.service.dto.ContactDTO;
import com.it4innov.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "adherent", source = "adherent", qualifiedByName = "personneId")
    ContactDTO toDto(Contact s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}
