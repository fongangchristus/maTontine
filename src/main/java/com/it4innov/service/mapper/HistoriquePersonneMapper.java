package com.it4innov.service.mapper;

import com.it4innov.domain.HistoriquePersonne;
import com.it4innov.domain.Personne;
import com.it4innov.service.dto.HistoriquePersonneDTO;
import com.it4innov.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoriquePersonne} and its DTO {@link HistoriquePersonneDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriquePersonneMapper extends EntityMapper<HistoriquePersonneDTO, HistoriquePersonne> {
    @Mapping(target = "personne", source = "personne", qualifiedByName = "personneId")
    HistoriquePersonneDTO toDto(HistoriquePersonne s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}
