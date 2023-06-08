package com.it4innov.service.mapper;

import com.it4innov.domain.CompteTontine;
import com.it4innov.domain.CotisationTontine;
import com.it4innov.domain.SessionTontine;
import com.it4innov.service.dto.CompteTontineDTO;
import com.it4innov.service.dto.CotisationTontineDTO;
import com.it4innov.service.dto.SessionTontineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CotisationTontine} and its DTO {@link CotisationTontineDTO}.
 */
@Mapper(componentModel = "spring")
public interface CotisationTontineMapper extends EntityMapper<CotisationTontineDTO, CotisationTontine> {
    @Mapping(target = "sessionTontine", source = "sessionTontine", qualifiedByName = "sessionTontineId")
    @Mapping(target = "compteTontine", source = "compteTontine", qualifiedByName = "compteTontineId")
    CotisationTontineDTO toDto(CotisationTontine s);

    @Named("sessionTontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SessionTontineDTO toDtoSessionTontineId(SessionTontine sessionTontine);

    @Named("compteTontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteTontineDTO toDtoCompteTontineId(CompteTontine compteTontine);
}
