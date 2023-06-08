package com.it4innov.service.impl;

import com.it4innov.domain.PaiementSanction;
import com.it4innov.repository.PaiementSanctionRepository;
import com.it4innov.service.PaiementSanctionService;
import com.it4innov.service.dto.PaiementSanctionDTO;
import com.it4innov.service.mapper.PaiementSanctionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaiementSanction}.
 */
@Service
@Transactional
public class PaiementSanctionServiceImpl implements PaiementSanctionService {

    private final Logger log = LoggerFactory.getLogger(PaiementSanctionServiceImpl.class);

    private final PaiementSanctionRepository paiementSanctionRepository;

    private final PaiementSanctionMapper paiementSanctionMapper;

    public PaiementSanctionServiceImpl(
        PaiementSanctionRepository paiementSanctionRepository,
        PaiementSanctionMapper paiementSanctionMapper
    ) {
        this.paiementSanctionRepository = paiementSanctionRepository;
        this.paiementSanctionMapper = paiementSanctionMapper;
    }

    @Override
    public PaiementSanctionDTO save(PaiementSanctionDTO paiementSanctionDTO) {
        log.debug("Request to save PaiementSanction : {}", paiementSanctionDTO);
        PaiementSanction paiementSanction = paiementSanctionMapper.toEntity(paiementSanctionDTO);
        paiementSanction = paiementSanctionRepository.save(paiementSanction);
        return paiementSanctionMapper.toDto(paiementSanction);
    }

    @Override
    public PaiementSanctionDTO update(PaiementSanctionDTO paiementSanctionDTO) {
        log.debug("Request to update PaiementSanction : {}", paiementSanctionDTO);
        PaiementSanction paiementSanction = paiementSanctionMapper.toEntity(paiementSanctionDTO);
        paiementSanction = paiementSanctionRepository.save(paiementSanction);
        return paiementSanctionMapper.toDto(paiementSanction);
    }

    @Override
    public Optional<PaiementSanctionDTO> partialUpdate(PaiementSanctionDTO paiementSanctionDTO) {
        log.debug("Request to partially update PaiementSanction : {}", paiementSanctionDTO);

        return paiementSanctionRepository
            .findById(paiementSanctionDTO.getId())
            .map(existingPaiementSanction -> {
                paiementSanctionMapper.partialUpdate(existingPaiementSanction, paiementSanctionDTO);

                return existingPaiementSanction;
            })
            .map(paiementSanctionRepository::save)
            .map(paiementSanctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementSanctionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaiementSanctions");
        return paiementSanctionRepository.findAll(pageable).map(paiementSanctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaiementSanctionDTO> findOne(Long id) {
        log.debug("Request to get PaiementSanction : {}", id);
        return paiementSanctionRepository.findById(id).map(paiementSanctionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaiementSanction : {}", id);
        paiementSanctionRepository.deleteById(id);
    }
}
