package com.it4innov.service.mapper;

import com.it4innov.domain.Adresse;
import com.it4innov.service.dto.AdresseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Adresse} and its DTO {@link AdresseDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdresseMapper extends EntityMapper<AdresseDTO, Adresse> {}
