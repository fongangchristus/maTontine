package com.it4innov.service.impl;

import com.it4innov.domain.Fonction;
import com.it4innov.repository.FonctionRepository;
import com.it4innov.service.FonctionService;
import com.it4innov.service.dto.FonctionDTO;
import com.it4innov.service.mapper.FonctionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fonction}.
 */
@Service
@Transactional
public class FonctionServiceImpl implements FonctionService {

    private final Logger log = LoggerFactory.getLogger(FonctionServiceImpl.class);

    private final FonctionRepository fonctionRepository;

    private final FonctionMapper fonctionMapper;

    public FonctionServiceImpl(FonctionRepository fonctionRepository, FonctionMapper fonctionMapper) {
        this.fonctionRepository = fonctionRepository;
        this.fonctionMapper = fonctionMapper;
    }

    @Override
    public FonctionDTO save(FonctionDTO fonctionDTO) {
        log.debug("Request to save Fonction : {}", fonctionDTO);
        Fonction fonction = fonctionMapper.toEntity(fonctionDTO);
        fonction = fonctionRepository.save(fonction);
        return fonctionMapper.toDto(fonction);
    }

    @Override
    public FonctionDTO update(FonctionDTO fonctionDTO) {
        log.debug("Request to update Fonction : {}", fonctionDTO);
        Fonction fonction = fonctionMapper.toEntity(fonctionDTO);
        fonction = fonctionRepository.save(fonction);
        return fonctionMapper.toDto(fonction);
    }

    @Override
    public Optional<FonctionDTO> partialUpdate(FonctionDTO fonctionDTO) {
        log.debug("Request to partially update Fonction : {}", fonctionDTO);

        return fonctionRepository
            .findById(fonctionDTO.getId())
            .map(existingFonction -> {
                fonctionMapper.partialUpdate(existingFonction, fonctionDTO);

                return existingFonction;
            })
            .map(fonctionRepository::save)
            .map(fonctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FonctionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fonctions");
        return fonctionRepository.findAll(pageable).map(fonctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FonctionDTO> findOne(Long id) {
        log.debug("Request to get Fonction : {}", id);
        return fonctionRepository.findById(id).map(fonctionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fonction : {}", id);
        fonctionRepository.deleteById(id);
    }
}
