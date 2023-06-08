package com.it4innov.service.mapper;

import com.it4innov.domain.Association;
import com.it4innov.domain.DocumentAssociation;
import com.it4innov.service.dto.AssociationDTO;
import com.it4innov.service.dto.DocumentAssociationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentAssociation} and its DTO {@link DocumentAssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentAssociationMapper extends EntityMapper<DocumentAssociationDTO, DocumentAssociation> {
    @Mapping(target = "association", source = "association", qualifiedByName = "associationId")
    DocumentAssociationDTO toDto(DocumentAssociation s);

    @Named("associationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssociationDTO toDtoAssociationId(Association association);
}
