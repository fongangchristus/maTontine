package com.it4innov.service.impl;

import com.it4innov.domain.TypeEvenement;
import com.it4innov.repository.TypeEvenementRepository;
import com.it4innov.service.TypeEvenementService;
import com.it4innov.service.dto.TypeEvenementDTO;
import com.it4innov.service.mapper.TypeEvenementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeEvenement}.
 */
@Service
@Transactional
public class TypeEvenementServiceImpl implements TypeEvenementService {

    private final Logger log = LoggerFactory.getLogger(TypeEvenementServiceImpl.class);

    private final TypeEvenementRepository typeEvenementRepository;

    private final TypeEvenementMapper typeEvenementMapper;

    public TypeEvenementServiceImpl(TypeEvenementRepository typeEvenementRepository, TypeEvenementMapper typeEvenementMapper) {
        this.typeEvenementRepository = typeEvenementRepository;
        this.typeEvenementMapper = typeEvenementMapper;
    }

    @Override
    public TypeEvenementDTO save(TypeEvenementDTO typeEvenementDTO) {
        log.debug("Request to save TypeEvenement : {}", typeEvenementDTO);
        TypeEvenement typeEvenement = typeEvenementMapper.toEntity(typeEvenementDTO);
        typeEvenement = typeEvenementRepository.save(typeEvenement);
        return typeEvenementMapper.toDto(typeEvenement);
    }

    @Override
    public TypeEvenementDTO update(TypeEvenementDTO typeEvenementDTO) {
        log.debug("Request to update TypeEvenement : {}", typeEvenementDTO);
        TypeEvenement typeEvenement = typeEvenementMapper.toEntity(typeEvenementDTO);
        typeEvenement = typeEvenementRepository.save(typeEvenement);
        return typeEvenementMapper.toDto(typeEvenement);
    }

    @Override
    public Optional<TypeEvenementDTO> partialUpdate(TypeEvenementDTO typeEvenementDTO) {
        log.debug("Request to partially update TypeEvenement : {}", typeEvenementDTO);

        return typeEvenementRepository
            .findById(typeEvenementDTO.getId())
            .map(existingTypeEvenement -> {
                typeEvenementMapper.partialUpdate(existingTypeEvenement, typeEvenementDTO);

                return existingTypeEvenement;
            })
            .map(typeEvenementRepository::save)
            .map(typeEvenementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeEvenementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeEvenements");
        return typeEvenementRepository.findAll(pageable).map(typeEvenementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeEvenementDTO> findOne(Long id) {
        log.debug("Request to get TypeEvenement : {}", id);
        return typeEvenementRepository.findById(id).map(typeEvenementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeEvenement : {}", id);
        typeEvenementRepository.deleteById(id);
    }
}
