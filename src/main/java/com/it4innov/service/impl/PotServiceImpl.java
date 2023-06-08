package com.it4innov.service.impl;

import com.it4innov.domain.Pot;
import com.it4innov.repository.PotRepository;
import com.it4innov.service.PotService;
import com.it4innov.service.dto.PotDTO;
import com.it4innov.service.mapper.PotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pot}.
 */
@Service
@Transactional
public class PotServiceImpl implements PotService {

    private final Logger log = LoggerFactory.getLogger(PotServiceImpl.class);

    private final PotRepository potRepository;

    private final PotMapper potMapper;

    public PotServiceImpl(PotRepository potRepository, PotMapper potMapper) {
        this.potRepository = potRepository;
        this.potMapper = potMapper;
    }

    @Override
    public PotDTO save(PotDTO potDTO) {
        log.debug("Request to save Pot : {}", potDTO);
        Pot pot = potMapper.toEntity(potDTO);
        pot = potRepository.save(pot);
        return potMapper.toDto(pot);
    }

    @Override
    public PotDTO update(PotDTO potDTO) {
        log.debug("Request to update Pot : {}", potDTO);
        Pot pot = potMapper.toEntity(potDTO);
        pot = potRepository.save(pot);
        return potMapper.toDto(pot);
    }

    @Override
    public Optional<PotDTO> partialUpdate(PotDTO potDTO) {
        log.debug("Request to partially update Pot : {}", potDTO);

        return potRepository
            .findById(potDTO.getId())
            .map(existingPot -> {
                potMapper.partialUpdate(existingPot, potDTO);

                return existingPot;
            })
            .map(potRepository::save)
            .map(potMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pots");
        return potRepository.findAll(pageable).map(potMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PotDTO> findOne(Long id) {
        log.debug("Request to get Pot : {}", id);
        return potRepository.findById(id).map(potMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pot : {}", id);
        potRepository.deleteById(id);
    }
}
