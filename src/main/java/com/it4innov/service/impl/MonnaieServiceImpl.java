package com.it4innov.service.impl;

import com.it4innov.domain.Monnaie;
import com.it4innov.repository.MonnaieRepository;
import com.it4innov.service.MonnaieService;
import com.it4innov.service.dto.MonnaieDTO;
import com.it4innov.service.mapper.MonnaieMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Monnaie}.
 */
@Service
@Transactional
public class MonnaieServiceImpl implements MonnaieService {

    private final Logger log = LoggerFactory.getLogger(MonnaieServiceImpl.class);

    private final MonnaieRepository monnaieRepository;

    private final MonnaieMapper monnaieMapper;

    public MonnaieServiceImpl(MonnaieRepository monnaieRepository, MonnaieMapper monnaieMapper) {
        this.monnaieRepository = monnaieRepository;
        this.monnaieMapper = monnaieMapper;
    }

    @Override
    public MonnaieDTO save(MonnaieDTO monnaieDTO) {
        log.debug("Request to save Monnaie : {}", monnaieDTO);
        Monnaie monnaie = monnaieMapper.toEntity(monnaieDTO);
        monnaie = monnaieRepository.save(monnaie);
        return monnaieMapper.toDto(monnaie);
    }

    @Override
    public MonnaieDTO update(MonnaieDTO monnaieDTO) {
        log.debug("Request to update Monnaie : {}", monnaieDTO);
        Monnaie monnaie = monnaieMapper.toEntity(monnaieDTO);
        monnaie = monnaieRepository.save(monnaie);
        return monnaieMapper.toDto(monnaie);
    }

    @Override
    public Optional<MonnaieDTO> partialUpdate(MonnaieDTO monnaieDTO) {
        log.debug("Request to partially update Monnaie : {}", monnaieDTO);

        return monnaieRepository
            .findById(monnaieDTO.getId())
            .map(existingMonnaie -> {
                monnaieMapper.partialUpdate(existingMonnaie, monnaieDTO);

                return existingMonnaie;
            })
            .map(monnaieRepository::save)
            .map(monnaieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonnaieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Monnaies");
        return monnaieRepository.findAll(pageable).map(monnaieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MonnaieDTO> findOne(Long id) {
        log.debug("Request to get Monnaie : {}", id);
        return monnaieRepository.findById(id).map(monnaieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Monnaie : {}", id);
        monnaieRepository.deleteById(id);
    }
}
