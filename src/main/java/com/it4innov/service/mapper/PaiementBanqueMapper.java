package com.it4innov.service.mapper;

import com.it4innov.domain.PaiementBanque;
import com.it4innov.service.dto.PaiementBanqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaiementBanque} and its DTO {@link PaiementBanqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementBanqueMapper extends EntityMapper<PaiementBanqueDTO, PaiementBanque> {}
