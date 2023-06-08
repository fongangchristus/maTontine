package com.it4innov.service.mapper;

import com.it4innov.domain.Adhesion;
import com.it4innov.domain.FormuleAdhesion;
import com.it4innov.service.dto.AdhesionDTO;
import com.it4innov.service.dto.FormuleAdhesionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormuleAdhesion} and its DTO {@link FormuleAdhesionDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormuleAdhesionMapper extends EntityMapper<FormuleAdhesionDTO, FormuleAdhesion> {
    @Mapping(target = "adhesion", source = "adhesion", qualifiedByName = "adhesionId")
    FormuleAdhesionDTO toDto(FormuleAdhesion s);

    @Named("adhesionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdhesionDTO toDtoAdhesionId(Adhesion adhesion);
}
