package com.it4innov.service.impl;

import com.it4innov.domain.TypePot;
import com.it4innov.repository.TypePotRepository;
import com.it4innov.service.TypePotService;
import com.it4innov.service.dto.TypePotDTO;
import com.it4innov.service.mapper.TypePotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypePot}.
 */
@Service
@Transactional
public class TypePotServiceImpl implements TypePotService {

    private final Logger log = LoggerFactory.getLogger(TypePotServiceImpl.class);

    private final TypePotRepository typePotRepository;

    private final TypePotMapper typePotMapper;

    public TypePotServiceImpl(TypePotRepository typePotRepository, TypePotMapper typePotMapper) {
        this.typePotRepository = typePotRepository;
        this.typePotMapper = typePotMapper;
    }

    @Override
    public TypePotDTO save(TypePotDTO typePotDTO) {
        log.debug("Request to save TypePot : {}", typePotDTO);
        TypePot typePot = typePotMapper.toEntity(typePotDTO);
        typePot = typePotRepository.save(typePot);
        return typePotMapper.toDto(typePot);
    }

    @Override
    public TypePotDTO update(TypePotDTO typePotDTO) {
        log.debug("Request to update TypePot : {}", typePotDTO);
        TypePot typePot = typePotMapper.toEntity(typePotDTO);
        typePot = typePotRepository.save(typePot);
        return typePotMapper.toDto(typePot);
    }

    @Override
    public Optional<TypePotDTO> partialUpdate(TypePotDTO typePotDTO) {
        log.debug("Request to partially update TypePot : {}", typePotDTO);

        return typePotRepository
            .findById(typePotDTO.getId())
            .map(existingTypePot -> {
                typePotMapper.partialUpdate(existingTypePot, typePotDTO);

                return existingTypePot;
            })
            .map(typePotRepository::save)
            .map(typePotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypePotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypePots");
        return typePotRepository.findAll(pageable).map(typePotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypePotDTO> findOne(Long id) {
        log.debug("Request to get TypePot : {}", id);
        return typePotRepository.findById(id).map(typePotMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypePot : {}", id);
        typePotRepository.deleteById(id);
    }
}
