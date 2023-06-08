package com.it4innov.service.mapper;

import com.it4innov.domain.Banque;
import com.it4innov.domain.CompteBanque;
import com.it4innov.service.dto.BanqueDTO;
import com.it4innov.service.dto.CompteBanqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompteBanque} and its DTO {@link CompteBanqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteBanqueMapper extends EntityMapper<CompteBanqueDTO, CompteBanque> {
    @Mapping(target = "banque", source = "banque", qualifiedByName = "banqueId")
    CompteBanqueDTO toDto(CompteBanque s);

    @Named("banqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BanqueDTO toDtoBanqueId(Banque banque);
}
