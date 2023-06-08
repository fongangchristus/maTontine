package com.it4innov.service.mapper;

import com.it4innov.domain.CotisationTontine;
import com.it4innov.domain.DecaissementTontine;
import com.it4innov.domain.PaiementTontine;
import com.it4innov.service.dto.CotisationTontineDTO;
import com.it4innov.service.dto.DecaissementTontineDTO;
import com.it4innov.service.dto.PaiementTontineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaiementTontine} and its DTO {@link PaiementTontineDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementTontineMapper extends EntityMapper<PaiementTontineDTO, PaiementTontine> {
    @Mapping(target = "cotisationTontine", source = "cotisationTontine", qualifiedByName = "cotisationTontineId")
    @Mapping(target = "decaissementTontine", source = "decaissementTontine", qualifiedByName = "decaissementTontineId")
    PaiementTontineDTO toDto(PaiementTontine s);

    @Named("cotisationTontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CotisationTontineDTO toDtoCotisationTontineId(CotisationTontine cotisationTontine);

    @Named("decaissementTontineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DecaissementTontineDTO toDtoDecaissementTontineId(DecaissementTontine decaissementTontine);
}
