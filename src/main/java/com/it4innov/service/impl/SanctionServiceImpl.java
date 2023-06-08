package com.it4innov.service.impl;

import com.it4innov.domain.Sanction;
import com.it4innov.repository.SanctionRepository;
import com.it4innov.service.SanctionService;
import com.it4innov.service.dto.SanctionDTO;
import com.it4innov.service.mapper.SanctionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sanction}.
 */
@Service
@Transactional
public class SanctionServiceImpl implements SanctionService {

    private final Logger log = LoggerFactory.getLogger(SanctionServiceImpl.class);

    private final SanctionRepository sanctionRepository;

    private final SanctionMapper sanctionMapper;

    public SanctionServiceImpl(SanctionRepository sanctionRepository, SanctionMapper sanctionMapper) {
        this.sanctionRepository = sanctionRepository;
        this.sanctionMapper = sanctionMapper;
    }

    @Override
    public SanctionDTO save(SanctionDTO sanctionDTO) {
        log.debug("Request to save Sanction : {}", sanctionDTO);
        Sanction sanction = sanctionMapper.toEntity(sanctionDTO);
        sanction = sanctionRepository.save(sanction);
        return sanctionMapper.toDto(sanction);
    }

    @Override
    public SanctionDTO update(SanctionDTO sanctionDTO) {
        log.debug("Request to update Sanction : {}", sanctionDTO);
        Sanction sanction = sanctionMapper.toEntity(sanctionDTO);
        sanction = sanctionRepository.save(sanction);
        return sanctionMapper.toDto(sanction);
    }

    @Override
    public Optional<SanctionDTO> partialUpdate(SanctionDTO sanctionDTO) {
        log.debug("Request to partially update Sanction : {}", sanctionDTO);

        return sanctionRepository
            .findById(sanctionDTO.getId())
            .map(existingSanction -> {
                sanctionMapper.partialUpdate(existingSanction, sanctionDTO);

                return existingSanction;
            })
            .map(sanctionRepository::save)
            .map(sanctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SanctionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sanctions");
        return sanctionRepository.findAll(pageable).map(sanctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SanctionDTO> findOne(Long id) {
        log.debug("Request to get Sanction : {}", id);
        return sanctionRepository.findById(id).map(sanctionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sanction : {}", id);
        sanctionRepository.deleteById(id);
    }
}
