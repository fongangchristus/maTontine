package com.it4innov.service.mapper;

import com.it4innov.domain.Banque;
import com.it4innov.domain.GestionnaireBanque;
import com.it4innov.service.dto.BanqueDTO;
import com.it4innov.service.dto.GestionnaireBanqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GestionnaireBanque} and its DTO {@link GestionnaireBanqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface GestionnaireBanqueMapper extends EntityMapper<GestionnaireBanqueDTO, GestionnaireBanque> {
    @Mapping(target = "banque", source = "banque", qualifiedByName = "banqueId")
    GestionnaireBanqueDTO toDto(GestionnaireBanque s);

    @Named("banqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BanqueDTO toDtoBanqueId(Banque banque);
}
