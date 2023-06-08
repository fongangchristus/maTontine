package com.it4innov.service.mapper;

import com.it4innov.domain.GestionnaireTontine;
import com.it4innov.domain.Tontine;
import com.it4innov.service.dto.GestionnaireTontineDTO;
import com.it4innov.service.dto.TontineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GestionnaireTontine} and its DTO {@link GestionnaireTontineDTO}.
 */
@Mapper(componentModel = "spring")
public interface GestionnaireTontineMapper extends EntityMapper<GestionnaireTontineDTO, GestionnaireTontine> {
    @Mapping(target = "tontine", source = "tontine", qualifiedByName = "tontineId")
    GestionnaireTontineDTO toDto(GestionnaireTontine s);

    @Named("tontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TontineDTO toDtoTontineId(Tontine tontine);
}
