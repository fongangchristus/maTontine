package com.it4innov.service.impl;

import com.it4innov.domain.SanctionConfiguration;
import com.it4innov.repository.SanctionConfigurationRepository;
import com.it4innov.service.SanctionConfigurationService;
import com.it4innov.service.dto.SanctionConfigurationDTO;
import com.it4innov.service.mapper.SanctionConfigurationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SanctionConfiguration}.
 */
@Service
@Transactional
public class SanctionConfigurationServiceImpl implements SanctionConfigurationService {

    private final Logger log = LoggerFactory.getLogger(SanctionConfigurationServiceImpl.class);

    private final SanctionConfigurationRepository sanctionConfigurationRepository;

    private final SanctionConfigurationMapper sanctionConfigurationMapper;

    public SanctionConfigurationServiceImpl(
        SanctionConfigurationRepository sanctionConfigurationRepository,
        SanctionConfigurationMapper sanctionConfigurationMapper
    ) {
        this.sanctionConfigurationRepository = sanctionConfigurationRepository;
        this.sanctionConfigurationMapper = sanctionConfigurationMapper;
    }

    @Override
    public SanctionConfigurationDTO save(SanctionConfigurationDTO sanctionConfigurationDTO) {
        log.debug("Request to save SanctionConfiguration : {}", sanctionConfigurationDTO);
        SanctionConfiguration sanctionConfiguration = sanctionConfigurationMapper.toEntity(sanctionConfigurationDTO);
        sanctionConfiguration = sanctionConfigurationRepository.save(sanctionConfiguration);
        return sanctionConfigurationMapper.toDto(sanctionConfiguration);
    }

    @Override
    public SanctionConfigurationDTO update(SanctionConfigurationDTO sanctionConfigurationDTO) {
        log.debug("Request to update SanctionConfiguration : {}", sanctionConfigurationDTO);
        SanctionConfiguration sanctionConfiguration = sanctionConfigurationMapper.toEntity(sanctionConfigurationDTO);
        sanctionConfiguration = sanctionConfigurationRepository.save(sanctionConfiguration);
        return sanctionConfigurationMapper.toDto(sanctionConfiguration);
    }

    @Override
    public Optional<SanctionConfigurationDTO> partialUpdate(SanctionConfigurationDTO sanctionConfigurationDTO) {
        log.debug("Request to partially update SanctionConfiguration : {}", sanctionConfigurationDTO);

        return sanctionConfigurationRepository
            .findById(sanctionConfigurationDTO.getId())
            .map(existingSanctionConfiguration -> {
                sanctionConfigurationMapper.partialUpdate(existingSanctionConfiguration, sanctionConfigurationDTO);

                return existingSanctionConfiguration;
            })
            .map(sanctionConfigurationRepository::save)
            .map(sanctionConfigurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SanctionConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SanctionConfigurations");
        return sanctionConfigurationRepository.findAll(pageable).map(sanctionConfigurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SanctionConfigurationDTO> findOne(Long id) {
        log.debug("Request to get SanctionConfiguration : {}", id);
        return sanctionConfigurationRepository.findById(id).map(sanctionConfigurationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SanctionConfiguration : {}", id);
        sanctionConfigurationRepository.deleteById(id);
    }
}
