package com.it4innov.service.impl;

import com.it4innov.domain.Tontine;
import com.it4innov.repository.TontineRepository;
import com.it4innov.service.TontineService;
import com.it4innov.service.dto.TontineDTO;
import com.it4innov.service.mapper.TontineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tontine}.
 */
@Service
@Transactional
public class TontineServiceImpl implements TontineService {

    private final Logger log = LoggerFactory.getLogger(TontineServiceImpl.class);

    private final TontineRepository tontineRepository;

    private final TontineMapper tontineMapper;

    public TontineServiceImpl(TontineRepository tontineRepository, TontineMapper tontineMapper) {
        this.tontineRepository = tontineRepository;
        this.tontineMapper = tontineMapper;
    }

    @Override
    public TontineDTO save(TontineDTO tontineDTO) {
        log.debug("Request to save Tontine : {}", tontineDTO);
        Tontine tontine = tontineMapper.toEntity(tontineDTO);
        tontine = tontineRepository.save(tontine);
        return tontineMapper.toDto(tontine);
    }

    @Override
    public TontineDTO update(TontineDTO tontineDTO) {
        log.debug("Request to update Tontine : {}", tontineDTO);
        Tontine tontine = tontineMapper.toEntity(tontineDTO);
        tontine = tontineRepository.save(tontine);
        return tontineMapper.toDto(tontine);
    }

    @Override
    public Optional<TontineDTO> partialUpdate(TontineDTO tontineDTO) {
        log.debug("Request to partially update Tontine : {}", tontineDTO);

        return tontineRepository
            .findById(tontineDTO.getId())
            .map(existingTontine -> {
                tontineMapper.partialUpdate(existingTontine, tontineDTO);

                return existingTontine;
            })
            .map(tontineRepository::save)
            .map(tontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TontineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tontines");
        return tontineRepository.findAll(pageable).map(tontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TontineDTO> findOne(Long id) {
        log.debug("Request to get Tontine : {}", id);
        return tontineRepository.findById(id).map(tontineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tontine : {}", id);
        tontineRepository.deleteById(id);
    }
}
