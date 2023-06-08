package com.it4innov.service.impl;

import com.it4innov.domain.CotisationTontine;
import com.it4innov.repository.CotisationTontineRepository;
import com.it4innov.service.CotisationTontineService;
import com.it4innov.service.dto.CotisationTontineDTO;
import com.it4innov.service.mapper.CotisationTontineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CotisationTontine}.
 */
@Service
@Transactional
public class CotisationTontineServiceImpl implements CotisationTontineService {

    private final Logger log = LoggerFactory.getLogger(CotisationTontineServiceImpl.class);

    private final CotisationTontineRepository cotisationTontineRepository;

    private final CotisationTontineMapper cotisationTontineMapper;

    public CotisationTontineServiceImpl(
        CotisationTontineRepository cotisationTontineRepository,
        CotisationTontineMapper cotisationTontineMapper
    ) {
        this.cotisationTontineRepository = cotisationTontineRepository;
        this.cotisationTontineMapper = cotisationTontineMapper;
    }

    @Override
    public CotisationTontineDTO save(CotisationTontineDTO cotisationTontineDTO) {
        log.debug("Request to save CotisationTontine : {}", cotisationTontineDTO);
        CotisationTontine cotisationTontine = cotisationTontineMapper.toEntity(cotisationTontineDTO);
        cotisationTontine = cotisationTontineRepository.save(cotisationTontine);
        return cotisationTontineMapper.toDto(cotisationTontine);
    }

    @Override
    public CotisationTontineDTO update(CotisationTontineDTO cotisationTontineDTO) {
        log.debug("Request to update CotisationTontine : {}", cotisationTontineDTO);
        CotisationTontine cotisationTontine = cotisationTontineMapper.toEntity(cotisationTontineDTO);
        cotisationTontine = cotisationTontineRepository.save(cotisationTontine);
        return cotisationTontineMapper.toDto(cotisationTontine);
    }

    @Override
    public Optional<CotisationTontineDTO> partialUpdate(CotisationTontineDTO cotisationTontineDTO) {
        log.debug("Request to partially update CotisationTontine : {}", cotisationTontineDTO);

        return cotisationTontineRepository
            .findById(cotisationTontineDTO.getId())
            .map(existingCotisationTontine -> {
                cotisationTontineMapper.partialUpdate(existingCotisationTontine, cotisationTontineDTO);

                return existingCotisationTontine;
            })
            .map(cotisationTontineRepository::save)
            .map(cotisationTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CotisationTontineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CotisationTontines");
        return cotisationTontineRepository.findAll(pageable).map(cotisationTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CotisationTontineDTO> findOne(Long id) {
        log.debug("Request to get CotisationTontine : {}", id);
        return cotisationTontineRepository.findById(id).map(cotisationTontineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CotisationTontine : {}", id);
        cotisationTontineRepository.deleteById(id);
    }
}
