package com.it4innov.service.mapper;

import com.it4innov.domain.Fonction;
import com.it4innov.domain.FonctionAdherent;
import com.it4innov.domain.Personne;
import com.it4innov.service.dto.FonctionAdherentDTO;
import com.it4innov.service.dto.FonctionDTO;
import com.it4innov.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FonctionAdherent} and its DTO {@link FonctionAdherentDTO}.
 */
@Mapper(componentModel = "spring")
public interface FonctionAdherentMapper extends EntityMapper<FonctionAdherentDTO, FonctionAdherent> {
    @Mapping(target = "adherent", source = "adherent", qualifiedByName = "personneId")
    @Mapping(target = "fonction", source = "fonction", qualifiedByName = "fonctionId")
    FonctionAdherentDTO toDto(FonctionAdherent s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);

    @Named("fonctionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FonctionDTO toDtoFonctionId(Fonction fonction);
}
