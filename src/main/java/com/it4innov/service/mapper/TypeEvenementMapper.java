package com.it4innov.service.mapper;

import com.it4innov.domain.TypeEvenement;
import com.it4innov.service.dto.TypeEvenementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeEvenement} and its DTO {@link TypeEvenementDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeEvenementMapper extends EntityMapper<TypeEvenementDTO, TypeEvenement> {}
