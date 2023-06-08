package com.it4innov.service.mapper;

import com.it4innov.domain.TypePot;
import com.it4innov.service.dto.TypePotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypePot} and its DTO {@link TypePotDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypePotMapper extends EntityMapper<TypePotDTO, TypePot> {}
