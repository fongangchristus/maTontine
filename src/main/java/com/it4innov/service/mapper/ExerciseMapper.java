package com.it4innov.service.mapper;

import com.it4innov.domain.Association;
import com.it4innov.domain.Exercise;
import com.it4innov.service.dto.AssociationDTO;
import com.it4innov.service.dto.ExerciseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exercise} and its DTO {@link ExerciseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExerciseMapper extends EntityMapper<ExerciseDTO, Exercise> {
    @Mapping(target = "association", source = "association", qualifiedByName = "associationId")
    ExerciseDTO toDto(Exercise s);

    @Named("associationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssociationDTO toDtoAssociationId(Association association);
}
