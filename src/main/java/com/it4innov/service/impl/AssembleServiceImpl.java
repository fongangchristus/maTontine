package com.it4innov.service.impl;

import com.it4innov.domain.Assemble;
import com.it4innov.repository.AssembleRepository;
import com.it4innov.service.AssembleService;
import com.it4innov.service.dto.AssembleDTO;
import com.it4innov.service.mapper.AssembleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Assemble}.
 */
@Service
@Transactional
public class AssembleServiceImpl implements AssembleService {

    private final Logger log = LoggerFactory.getLogger(AssembleServiceImpl.class);

    private final AssembleRepository assembleRepository;

    private final AssembleMapper assembleMapper;

    public AssembleServiceImpl(AssembleRepository assembleRepository, AssembleMapper assembleMapper) {
        this.assembleRepository = assembleRepository;
        this.assembleMapper = assembleMapper;
    }

    @Override
    public AssembleDTO save(AssembleDTO assembleDTO) {
        log.debug("Request to save Assemble : {}", assembleDTO);
        Assemble assemble = assembleMapper.toEntity(assembleDTO);
        assemble = assembleRepository.save(assemble);
        return assembleMapper.toDto(assemble);
    }

    @Override
    public AssembleDTO update(AssembleDTO assembleDTO) {
        log.debug("Request to update Assemble : {}", assembleDTO);
        Assemble assemble = assembleMapper.toEntity(assembleDTO);
        assemble = assembleRepository.save(assemble);
        return assembleMapper.toDto(assemble);
    }

    @Override
    public Optional<AssembleDTO> partialUpdate(AssembleDTO assembleDTO) {
        log.debug("Request to partially update Assemble : {}", assembleDTO);

        return assembleRepository
            .findById(assembleDTO.getId())
            .map(existingAssemble -> {
                assembleMapper.partialUpdate(existingAssemble, assembleDTO);

                return existingAssemble;
            })
            .map(assembleRepository::save)
            .map(assembleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssembleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Assembles");
        return assembleRepository.findAll(pageable).map(assembleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssembleDTO> findOne(Long id) {
        log.debug("Request to get Assemble : {}", id);
        return assembleRepository.findById(id).map(assembleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Assemble : {}", id);
        assembleRepository.deleteById(id);
    }
}
