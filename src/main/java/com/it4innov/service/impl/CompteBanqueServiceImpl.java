package com.it4innov.service.impl;

import com.it4innov.domain.CompteBanque;
import com.it4innov.repository.CompteBanqueRepository;
import com.it4innov.service.CompteBanqueService;
import com.it4innov.service.dto.CompteBanqueDTO;
import com.it4innov.service.mapper.CompteBanqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompteBanque}.
 */
@Service
@Transactional
public class CompteBanqueServiceImpl implements CompteBanqueService {

    private final Logger log = LoggerFactory.getLogger(CompteBanqueServiceImpl.class);

    private final CompteBanqueRepository compteBanqueRepository;

    private final CompteBanqueMapper compteBanqueMapper;

    public CompteBanqueServiceImpl(CompteBanqueRepository compteBanqueRepository, CompteBanqueMapper compteBanqueMapper) {
        this.compteBanqueRepository = compteBanqueRepository;
        this.compteBanqueMapper = compteBanqueMapper;
    }

    @Override
    public CompteBanqueDTO save(CompteBanqueDTO compteBanqueDTO) {
        log.debug("Request to save CompteBanque : {}", compteBanqueDTO);
        CompteBanque compteBanque = compteBanqueMapper.toEntity(compteBanqueDTO);
        compteBanque = compteBanqueRepository.save(compteBanque);
        return compteBanqueMapper.toDto(compteBanque);
    }

    @Override
    public CompteBanqueDTO update(CompteBanqueDTO compteBanqueDTO) {
        log.debug("Request to update CompteBanque : {}", compteBanqueDTO);
        CompteBanque compteBanque = compteBanqueMapper.toEntity(compteBanqueDTO);
        compteBanque = compteBanqueRepository.save(compteBanque);
        return compteBanqueMapper.toDto(compteBanque);
    }

    @Override
    public Optional<CompteBanqueDTO> partialUpdate(CompteBanqueDTO compteBanqueDTO) {
        log.debug("Request to partially update CompteBanque : {}", compteBanqueDTO);

        return compteBanqueRepository
            .findById(compteBanqueDTO.getId())
            .map(existingCompteBanque -> {
                compteBanqueMapper.partialUpdate(existingCompteBanque, compteBanqueDTO);

                return existingCompteBanque;
            })
            .map(compteBanqueRepository::save)
            .map(compteBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompteBanqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompteBanques");
        return compteBanqueRepository.findAll(pageable).map(compteBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompteBanqueDTO> findOne(Long id) {
        log.debug("Request to get CompteBanque : {}", id);
        return compteBanqueRepository.findById(id).map(compteBanqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompteBanque : {}", id);
        compteBanqueRepository.deleteById(id);
    }
}
