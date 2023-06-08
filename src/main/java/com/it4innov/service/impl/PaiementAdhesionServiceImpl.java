package com.it4innov.service.impl;

import com.it4innov.domain.PaiementAdhesion;
import com.it4innov.repository.PaiementAdhesionRepository;
import com.it4innov.service.PaiementAdhesionService;
import com.it4innov.service.dto.PaiementAdhesionDTO;
import com.it4innov.service.mapper.PaiementAdhesionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaiementAdhesion}.
 */
@Service
@Transactional
public class PaiementAdhesionServiceImpl implements PaiementAdhesionService {

    private final Logger log = LoggerFactory.getLogger(PaiementAdhesionServiceImpl.class);

    private final PaiementAdhesionRepository paiementAdhesionRepository;

    private final PaiementAdhesionMapper paiementAdhesionMapper;

    public PaiementAdhesionServiceImpl(
        PaiementAdhesionRepository paiementAdhesionRepository,
        PaiementAdhesionMapper paiementAdhesionMapper
    ) {
        this.paiementAdhesionRepository = paiementAdhesionRepository;
        this.paiementAdhesionMapper = paiementAdhesionMapper;
    }

    @Override
    public PaiementAdhesionDTO save(PaiementAdhesionDTO paiementAdhesionDTO) {
        log.debug("Request to save PaiementAdhesion : {}", paiementAdhesionDTO);
        PaiementAdhesion paiementAdhesion = paiementAdhesionMapper.toEntity(paiementAdhesionDTO);
        paiementAdhesion = paiementAdhesionRepository.save(paiementAdhesion);
        return paiementAdhesionMapper.toDto(paiementAdhesion);
    }

    @Override
    public PaiementAdhesionDTO update(PaiementAdhesionDTO paiementAdhesionDTO) {
        log.debug("Request to update PaiementAdhesion : {}", paiementAdhesionDTO);
        PaiementAdhesion paiementAdhesion = paiementAdhesionMapper.toEntity(paiementAdhesionDTO);
        paiementAdhesion = paiementAdhesionRepository.save(paiementAdhesion);
        return paiementAdhesionMapper.toDto(paiementAdhesion);
    }

    @Override
    public Optional<PaiementAdhesionDTO> partialUpdate(PaiementAdhesionDTO paiementAdhesionDTO) {
        log.debug("Request to partially update PaiementAdhesion : {}", paiementAdhesionDTO);

        return paiementAdhesionRepository
            .findById(paiementAdhesionDTO.getId())
            .map(existingPaiementAdhesion -> {
                paiementAdhesionMapper.partialUpdate(existingPaiementAdhesion, paiementAdhesionDTO);

                return existingPaiementAdhesion;
            })
            .map(paiementAdhesionRepository::save)
            .map(paiementAdhesionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementAdhesionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaiementAdhesions");
        return paiementAdhesionRepository.findAll(pageable).map(paiementAdhesionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaiementAdhesionDTO> findOne(Long id) {
        log.debug("Request to get PaiementAdhesion : {}", id);
        return paiementAdhesionRepository.findById(id).map(paiementAdhesionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaiementAdhesion : {}", id);
        paiementAdhesionRepository.deleteById(id);
    }
}
