package com.it4innov.service.mapper;

import com.it4innov.domain.CommentairePot;
import com.it4innov.domain.Pot;
import com.it4innov.service.dto.CommentairePotDTO;
import com.it4innov.service.dto.PotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentairePot} and its DTO {@link CommentairePotDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentairePotMapper extends EntityMapper<CommentairePotDTO, CommentairePot> {
    @Mapping(target = "pot", source = "pot", qualifiedByName = "potId")
    CommentairePotDTO toDto(CommentairePot s);

    @Named("potId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PotDTO toDtoPotId(Pot pot);
}
