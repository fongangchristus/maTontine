package com.it4innov.service.mapper;

import com.it4innov.domain.CompteBanque;
import com.it4innov.domain.DecaisementBanque;
import com.it4innov.service.dto.CompteBanqueDTO;
import com.it4innov.service.dto.DecaisementBanqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DecaisementBanque} and its DTO {@link DecaisementBanqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface DecaisementBanqueMapper extends EntityMapper<DecaisementBanqueDTO, DecaisementBanque> {
    @Mapping(target = "compteBanque", source = "compteBanque", qualifiedByName = "compteBanqueId")
    DecaisementBanqueDTO toDto(DecaisementBanque s);

    @Named("compteBanqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteBanqueDTO toDtoCompteBanqueId(CompteBanque compteBanque);
}
