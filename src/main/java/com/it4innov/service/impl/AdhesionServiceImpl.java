package com.it4innov.service.impl;

import com.it4innov.domain.Adhesion;
import com.it4innov.repository.AdhesionRepository;
import com.it4innov.service.AdhesionService;
import com.it4innov.service.dto.AdhesionDTO;
import com.it4innov.service.mapper.AdhesionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Adhesion}.
 */
@Service
@Transactional
public class AdhesionServiceImpl implements AdhesionService {

    private final Logger log = LoggerFactory.getLogger(AdhesionServiceImpl.class);

    private final AdhesionRepository adhesionRepository;

    private final AdhesionMapper adhesionMapper;

    public AdhesionServiceImpl(AdhesionRepository adhesionRepository, AdhesionMapper adhesionMapper) {
        this.adhesionRepository = adhesionRepository;
        this.adhesionMapper = adhesionMapper;
    }

    @Override
    public AdhesionDTO save(AdhesionDTO adhesionDTO) {
        log.debug("Request to save Adhesion : {}", adhesionDTO);
        Adhesion adhesion = adhesionMapper.toEntity(adhesionDTO);
        adhesion = adhesionRepository.save(adhesion);
        return adhesionMapper.toDto(adhesion);
    }

    @Override
    public AdhesionDTO update(AdhesionDTO adhesionDTO) {
        log.debug("Request to update Adhesion : {}", adhesionDTO);
        Adhesion adhesion = adhesionMapper.toEntity(adhesionDTO);
        adhesion = adhesionRepository.save(adhesion);
        return adhesionMapper.toDto(adhesion);
    }

    @Override
    public Optional<AdhesionDTO> partialUpdate(AdhesionDTO adhesionDTO) {
        log.debug("Request to partially update Adhesion : {}", adhesionDTO);

        return adhesionRepository
            .findById(adhesionDTO.getId())
            .map(existingAdhesion -> {
                adhesionMapper.partialUpdate(existingAdhesion, adhesionDTO);

                return existingAdhesion;
            })
            .map(adhesionRepository::save)
            .map(adhesionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdhesionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Adhesions");
        return adhesionRepository.findAll(pageable).map(adhesionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdhesionDTO> findOne(Long id) {
        log.debug("Request to get Adhesion : {}", id);
        return adhesionRepository.findById(id).map(adhesionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Adhesion : {}", id);
        adhesionRepository.deleteById(id);
    }
}
