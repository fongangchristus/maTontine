package com.it4innov.service.impl;

import com.it4innov.domain.CompteTontine;
import com.it4innov.repository.CompteTontineRepository;
import com.it4innov.service.CompteTontineService;
import com.it4innov.service.dto.CompteTontineDTO;
import com.it4innov.service.mapper.CompteTontineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompteTontine}.
 */
@Service
@Transactional
public class CompteTontineServiceImpl implements CompteTontineService {

    private final Logger log = LoggerFactory.getLogger(CompteTontineServiceImpl.class);

    private final CompteTontineRepository compteTontineRepository;

    private final CompteTontineMapper compteTontineMapper;

    public CompteTontineServiceImpl(CompteTontineRepository compteTontineRepository, CompteTontineMapper compteTontineMapper) {
        this.compteTontineRepository = compteTontineRepository;
        this.compteTontineMapper = compteTontineMapper;
    }

    @Override
    public CompteTontineDTO save(CompteTontineDTO compteTontineDTO) {
        log.debug("Request to save CompteTontine : {}", compteTontineDTO);
        CompteTontine compteTontine = compteTontineMapper.toEntity(compteTontineDTO);
        compteTontine = compteTontineRepository.save(compteTontine);
        return compteTontineMapper.toDto(compteTontine);
    }

    @Override
    public CompteTontineDTO update(CompteTontineDTO compteTontineDTO) {
        log.debug("Request to update CompteTontine : {}", compteTontineDTO);
        CompteTontine compteTontine = compteTontineMapper.toEntity(compteTontineDTO);
        compteTontine = compteTontineRepository.save(compteTontine);
        return compteTontineMapper.toDto(compteTontine);
    }

    @Override
    public Optional<CompteTontineDTO> partialUpdate(CompteTontineDTO compteTontineDTO) {
        log.debug("Request to partially update CompteTontine : {}", compteTontineDTO);

        return compteTontineRepository
            .findById(compteTontineDTO.getId())
            .map(existingCompteTontine -> {
                compteTontineMapper.partialUpdate(existingCompteTontine, compteTontineDTO);

                return existingCompteTontine;
            })
            .map(compteTontineRepository::save)
            .map(compteTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompteTontineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompteTontines");
        return compteTontineRepository.findAll(pageable).map(compteTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompteTontineDTO> findOne(Long id) {
        log.debug("Request to get CompteTontine : {}", id);
        return compteTontineRepository.findById(id).map(compteTontineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompteTontine : {}", id);
        compteTontineRepository.deleteById(id);
    }
}
