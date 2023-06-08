package com.it4innov.service.mapper;

import com.it4innov.domain.FichePresence;
import com.it4innov.domain.Personne;
import com.it4innov.domain.Presence;
import com.it4innov.service.dto.FichePresenceDTO;
import com.it4innov.service.dto.PersonneDTO;
import com.it4innov.service.dto.PresenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Presence} and its DTO {@link PresenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PresenceMapper extends EntityMapper<PresenceDTO, Presence> {
    @Mapping(target = "fichePresence", source = "fichePresence", qualifiedByName = "fichePresenceId")
    @Mapping(target = "adherant", source = "adherant", qualifiedByName = "personneId")
    PresenceDTO toDto(Presence s);

    @Named("fichePresenceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FichePresenceDTO toDtoFichePresenceId(FichePresence fichePresence);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}
