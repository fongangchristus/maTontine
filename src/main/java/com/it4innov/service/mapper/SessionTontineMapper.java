package com.it4innov.service.mapper;

import com.it4innov.domain.SessionTontine;
import com.it4innov.domain.Tontine;
import com.it4innov.service.dto.SessionTontineDTO;
import com.it4innov.service.dto.TontineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SessionTontine} and its DTO {@link SessionTontineDTO}.
 */
@Mapper(componentModel = "spring")
public interface SessionTontineMapper extends EntityMapper<SessionTontineDTO, SessionTontine> {
    @Mapping(target = "tontine", source = "tontine", qualifiedByName = "tontineId")
    SessionTontineDTO toDto(SessionTontine s);

    @Named("tontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TontineDTO toDtoTontineId(Tontine tontine);
}
