package com.it4innov.service.mapper;

import com.it4innov.domain.CompteBanque;
import com.it4innov.domain.CotisationBanque;
import com.it4innov.service.dto.CompteBanqueDTO;
import com.it4innov.service.dto.CotisationBanqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CotisationBanque} and its DTO {@link CotisationBanqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface CotisationBanqueMapper extends EntityMapper<CotisationBanqueDTO, CotisationBanque> {
    @Mapping(target = "compteBanque", source = "compteBanque", qualifiedByName = "compteBanqueId")
    CotisationBanqueDTO toDto(CotisationBanque s);

    @Named("compteBanqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteBanqueDTO toDtoCompteBanqueId(CompteBanque compteBanque);
}
