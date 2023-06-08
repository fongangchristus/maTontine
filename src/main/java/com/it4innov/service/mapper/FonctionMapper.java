package com.it4innov.service.mapper;

import com.it4innov.domain.Fonction;
import com.it4innov.service.dto.FonctionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fonction} and its DTO {@link FonctionDTO}.
 */
@Mapper(componentModel = "spring")
public interface FonctionMapper extends EntityMapper<FonctionDTO, Fonction> {}
