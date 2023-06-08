package com.it4innov.service.impl;

import com.it4innov.domain.CompteRIB;
import com.it4innov.repository.CompteRIBRepository;
import com.it4innov.service.CompteRIBService;
import com.it4innov.service.dto.CompteRIBDTO;
import com.it4innov.service.mapper.CompteRIBMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompteRIB}.
 */
@Service
@Transactional
public class CompteRIBServiceImpl implements CompteRIBService {

    private final Logger log = LoggerFactory.getLogger(CompteRIBServiceImpl.class);

    private final CompteRIBRepository compteRIBRepository;

    private final CompteRIBMapper compteRIBMapper;

    public CompteRIBServiceImpl(CompteRIBRepository compteRIBRepository, CompteRIBMapper compteRIBMapper) {
        this.compteRIBRepository = compteRIBRepository;
        this.compteRIBMapper = compteRIBMapper;
    }

    @Override
    public CompteRIBDTO save(CompteRIBDTO compteRIBDTO) {
        log.debug("Request to save CompteRIB : {}", compteRIBDTO);
        CompteRIB compteRIB = compteRIBMapper.toEntity(compteRIBDTO);
        compteRIB = compteRIBRepository.save(compteRIB);
        return compteRIBMapper.toDto(compteRIB);
    }

    @Override
    public CompteRIBDTO update(CompteRIBDTO compteRIBDTO) {
        log.debug("Request to update CompteRIB : {}", compteRIBDTO);
        CompteRIB compteRIB = compteRIBMapper.toEntity(compteRIBDTO);
        compteRIB = compteRIBRepository.save(compteRIB);
        return compteRIBMapper.toDto(compteRIB);
    }

    @Override
    public Optional<CompteRIBDTO> partialUpdate(CompteRIBDTO compteRIBDTO) {
        log.debug("Request to partially update CompteRIB : {}", compteRIBDTO);

        return compteRIBRepository
            .findById(compteRIBDTO.getId())
            .map(existingCompteRIB -> {
                compteRIBMapper.partialUpdate(existingCompteRIB, compteRIBDTO);

                return existingCompteRIB;
            })
            .map(compteRIBRepository::save)
            .map(compteRIBMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompteRIBDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompteRIBS");
        return compteRIBRepository.findAll(pageable).map(compteRIBMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompteRIBDTO> findOne(Long id) {
        log.debug("Request to get CompteRIB : {}", id);
        return compteRIBRepository.findById(id).map(compteRIBMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompteRIB : {}", id);
        compteRIBRepository.deleteById(id);
    }
}
