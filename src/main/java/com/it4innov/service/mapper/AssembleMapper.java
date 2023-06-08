package com.it4innov.service.mapper;

import com.it4innov.domain.Assemble;
import com.it4innov.service.dto.AssembleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assemble} and its DTO {@link AssembleDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssembleMapper extends EntityMapper<AssembleDTO, Assemble> {}
