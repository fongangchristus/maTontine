package com.it4innov.service.impl;

import com.it4innov.domain.PaiementTontine;
import com.it4innov.repository.PaiementTontineRepository;
import com.it4innov.service.PaiementTontineService;
import com.it4innov.service.dto.PaiementTontineDTO;
import com.it4innov.service.mapper.PaiementTontineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaiementTontine}.
 */
@Service
@Transactional
public class PaiementTontineServiceImpl implements PaiementTontineService {

    private final Logger log = LoggerFactory.getLogger(PaiementTontineServiceImpl.class);

    private final PaiementTontineRepository paiementTontineRepository;

    private final PaiementTontineMapper paiementTontineMapper;

    public PaiementTontineServiceImpl(PaiementTontineRepository paiementTontineRepository, PaiementTontineMapper paiementTontineMapper) {
        this.paiementTontineRepository = paiementTontineRepository;
        this.paiementTontineMapper = paiementTontineMapper;
    }

    @Override
    public PaiementTontineDTO save(PaiementTontineDTO paiementTontineDTO) {
        log.debug("Request to save PaiementTontine : {}", paiementTontineDTO);
        PaiementTontine paiementTontine = paiementTontineMapper.toEntity(paiementTontineDTO);
        paiementTontine = paiementTontineRepository.save(paiementTontine);
        return paiementTontineMapper.toDto(paiementTontine);
    }

    @Override
    public PaiementTontineDTO update(PaiementTontineDTO paiementTontineDTO) {
        log.debug("Request to update PaiementTontine : {}", paiementTontineDTO);
        PaiementTontine paiementTontine = paiementTontineMapper.toEntity(paiementTontineDTO);
        paiementTontine = paiementTontineRepository.save(paiementTontine);
        return paiementTontineMapper.toDto(paiementTontine);
    }

    @Override
    public Optional<PaiementTontineDTO> partialUpdate(PaiementTontineDTO paiementTontineDTO) {
        log.debug("Request to partially update PaiementTontine : {}", paiementTontineDTO);

        return paiementTontineRepository
            .findById(paiementTontineDTO.getId())
            .map(existingPaiementTontine -> {
                paiementTontineMapper.partialUpdate(existingPaiementTontine, paiementTontineDTO);

                return existingPaiementTontine;
            })
            .map(paiementTontineRepository::save)
            .map(paiementTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementTontineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaiementTontines");
        return paiementTontineRepository.findAll(pageable).map(paiementTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaiementTontineDTO> findOne(Long id) {
        log.debug("Request to get PaiementTontine : {}", id);
        return paiementTontineRepository.findById(id).map(paiementTontineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaiementTontine : {}", id);
        paiementTontineRepository.deleteById(id);
    }
}
