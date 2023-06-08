package com.it4innov.service.mapper;

import com.it4innov.domain.Tontine;
import com.it4innov.service.dto.TontineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tontine} and its DTO {@link TontineDTO}.
 */
@Mapper(componentModel = "spring")
public interface TontineMapper extends EntityMapper<TontineDTO, Tontine> {}
