package com.it4innov.service.mapper;

import com.it4innov.domain.CompteTontine;
import com.it4innov.domain.DecaissementTontine;
import com.it4innov.domain.SessionTontine;
import com.it4innov.service.dto.CompteTontineDTO;
import com.it4innov.service.dto.DecaissementTontineDTO;
import com.it4innov.service.dto.SessionTontineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DecaissementTontine} and its DTO {@link DecaissementTontineDTO}.
 */
@Mapper(componentModel = "spring")
public interface DecaissementTontineMapper extends EntityMapper<DecaissementTontineDTO, DecaissementTontine> {
    @Mapping(target = "tontine", source = "tontine", qualifiedByName = "sessionTontineId")
    @Mapping(target = "compteTontine", source = "compteTontine", qualifiedByName = "compteTontineId")
    DecaissementTontineDTO toDto(DecaissementTontine s);

    @Named("sessionTontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SessionTontineDTO toDtoSessionTontineId(SessionTontine sessionTontine);

    @Named("compteTontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteTontineDTO toDtoCompteTontineId(CompteTontine compteTontine);
}
