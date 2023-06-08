package com.it4innov.service.mapper;

import com.it4innov.domain.Banque;
import com.it4innov.service.dto.BanqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Banque} and its DTO {@link BanqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface BanqueMapper extends EntityMapper<BanqueDTO, Banque> {}
