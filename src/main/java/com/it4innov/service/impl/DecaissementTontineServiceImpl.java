package com.it4innov.service.impl;

import com.it4innov.domain.DecaissementTontine;
import com.it4innov.repository.DecaissementTontineRepository;
import com.it4innov.service.DecaissementTontineService;
import com.it4innov.service.dto.DecaissementTontineDTO;
import com.it4innov.service.mapper.DecaissementTontineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DecaissementTontine}.
 */
@Service
@Transactional
public class DecaissementTontineServiceImpl implements DecaissementTontineService {

    private final Logger log = LoggerFactory.getLogger(DecaissementTontineServiceImpl.class);

    private final DecaissementTontineRepository decaissementTontineRepository;

    private final DecaissementTontineMapper decaissementTontineMapper;

    public DecaissementTontineServiceImpl(
        DecaissementTontineRepository decaissementTontineRepository,
        DecaissementTontineMapper decaissementTontineMapper
    ) {
        this.decaissementTontineRepository = decaissementTontineRepository;
        this.decaissementTontineMapper = decaissementTontineMapper;
    }

    @Override
    public DecaissementTontineDTO save(DecaissementTontineDTO decaissementTontineDTO) {
        log.debug("Request to save DecaissementTontine : {}", decaissementTontineDTO);
        DecaissementTontine decaissementTontine = decaissementTontineMapper.toEntity(decaissementTontineDTO);
        decaissementTontine = decaissementTontineRepository.save(decaissementTontine);
        return decaissementTontineMapper.toDto(decaissementTontine);
    }

    @Override
    public DecaissementTontineDTO update(DecaissementTontineDTO decaissementTontineDTO) {
        log.debug("Request to update DecaissementTontine : {}", decaissementTontineDTO);
        DecaissementTontine decaissementTontine = decaissementTontineMapper.toEntity(decaissementTontineDTO);
        decaissementTontine = decaissementTontineRepository.save(decaissementTontine);
        return decaissementTontineMapper.toDto(decaissementTontine);
    }

    @Override
    public Optional<DecaissementTontineDTO> partialUpdate(DecaissementTontineDTO decaissementTontineDTO) {
        log.debug("Request to partially update DecaissementTontine : {}", decaissementTontineDTO);

        return decaissementTontineRepository
            .findById(decaissementTontineDTO.getId())
            .map(existingDecaissementTontine -> {
                decaissementTontineMapper.partialUpdate(existingDecaissementTontine, decaissementTontineDTO);

                return existingDecaissementTontine;
            })
            .map(decaissementTontineRepository::save)
            .map(decaissementTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DecaissementTontineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DecaissementTontines");
        return decaissementTontineRepository.findAll(pageable).map(decaissementTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DecaissementTontineDTO> findOne(Long id) {
        log.debug("Request to get DecaissementTontine : {}", id);
        return decaissementTontineRepository.findById(id).map(decaissementTontineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DecaissementTontine : {}", id);
        decaissementTontineRepository.deleteById(id);
    }
}
