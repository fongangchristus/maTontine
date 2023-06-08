package com.it4innov.service.mapper;

import com.it4innov.domain.Adhesion;
import com.it4innov.service.dto.AdhesionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Adhesion} and its DTO {@link AdhesionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdhesionMapper extends EntityMapper<AdhesionDTO, Adhesion> {}
