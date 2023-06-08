package com.it4innov.service.impl;

import com.it4innov.domain.GestionnaireTontine;
import com.it4innov.repository.GestionnaireTontineRepository;
import com.it4innov.service.GestionnaireTontineService;
import com.it4innov.service.dto.GestionnaireTontineDTO;
import com.it4innov.service.mapper.GestionnaireTontineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GestionnaireTontine}.
 */
@Service
@Transactional
public class GestionnaireTontineServiceImpl implements GestionnaireTontineService {

    private final Logger log = LoggerFactory.getLogger(GestionnaireTontineServiceImpl.class);

    private final GestionnaireTontineRepository gestionnaireTontineRepository;

    private final GestionnaireTontineMapper gestionnaireTontineMapper;

    public GestionnaireTontineServiceImpl(
        GestionnaireTontineRepository gestionnaireTontineRepository,
        GestionnaireTontineMapper gestionnaireTontineMapper
    ) {
        this.gestionnaireTontineRepository = gestionnaireTontineRepository;
        this.gestionnaireTontineMapper = gestionnaireTontineMapper;
    }

    @Override
    public GestionnaireTontineDTO save(GestionnaireTontineDTO gestionnaireTontineDTO) {
        log.debug("Request to save GestionnaireTontine : {}", gestionnaireTontineDTO);
        GestionnaireTontine gestionnaireTontine = gestionnaireTontineMapper.toEntity(gestionnaireTontineDTO);
        gestionnaireTontine = gestionnaireTontineRepository.save(gestionnaireTontine);
        return gestionnaireTontineMapper.toDto(gestionnaireTontine);
    }

    @Override
    public GestionnaireTontineDTO update(GestionnaireTontineDTO gestionnaireTontineDTO) {
        log.debug("Request to update GestionnaireTontine : {}", gestionnaireTontineDTO);
        GestionnaireTontine gestionnaireTontine = gestionnaireTontineMapper.toEntity(gestionnaireTontineDTO);
        gestionnaireTontine = gestionnaireTontineRepository.save(gestionnaireTontine);
        return gestionnaireTontineMapper.toDto(gestionnaireTontine);
    }

    @Override
    public Optional<GestionnaireTontineDTO> partialUpdate(GestionnaireTontineDTO gestionnaireTontineDTO) {
        log.debug("Request to partially update GestionnaireTontine : {}", gestionnaireTontineDTO);

        return gestionnaireTontineRepository
            .findById(gestionnaireTontineDTO.getId())
            .map(existingGestionnaireTontine -> {
                gestionnaireTontineMapper.partialUpdate(existingGestionnaireTontine, gestionnaireTontineDTO);

                return existingGestionnaireTontine;
            })
            .map(gestionnaireTontineRepository::save)
            .map(gestionnaireTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GestionnaireTontineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GestionnaireTontines");
        return gestionnaireTontineRepository.findAll(pageable).map(gestionnaireTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GestionnaireTontineDTO> findOne(Long id) {
        log.debug("Request to get GestionnaireTontine : {}", id);
        return gestionnaireTontineRepository.findById(id).map(gestionnaireTontineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GestionnaireTontine : {}", id);
        gestionnaireTontineRepository.deleteById(id);
    }
}
