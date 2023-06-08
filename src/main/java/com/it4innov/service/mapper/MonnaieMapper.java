package com.it4innov.service.mapper;

import com.it4innov.domain.Monnaie;
import com.it4innov.service.dto.MonnaieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Monnaie} and its DTO {@link MonnaieDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonnaieMapper extends EntityMapper<MonnaieDTO, Monnaie> {}
