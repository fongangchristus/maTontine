package com.it4innov.service.mapper;

import com.it4innov.domain.CompteTontine;
import com.it4innov.domain.Tontine;
import com.it4innov.service.dto.CompteTontineDTO;
import com.it4innov.service.dto.TontineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompteTontine} and its DTO {@link CompteTontineDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteTontineMapper extends EntityMapper<CompteTontineDTO, CompteTontine> {
    @Mapping(target = "tontine", source = "tontine", qualifiedByName = "tontineId")
    CompteTontineDTO toDto(CompteTontine s);

    @Named("tontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TontineDTO toDtoTontineId(Tontine tontine);
}
