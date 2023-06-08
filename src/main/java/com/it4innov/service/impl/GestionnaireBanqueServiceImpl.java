package com.it4innov.service.impl;

import com.it4innov.domain.GestionnaireBanque;
import com.it4innov.repository.GestionnaireBanqueRepository;
import com.it4innov.service.GestionnaireBanqueService;
import com.it4innov.service.dto.GestionnaireBanqueDTO;
import com.it4innov.service.mapper.GestionnaireBanqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GestionnaireBanque}.
 */
@Service
@Transactional
public class GestionnaireBanqueServiceImpl implements GestionnaireBanqueService {

    private final Logger log = LoggerFactory.getLogger(GestionnaireBanqueServiceImpl.class);

    private final GestionnaireBanqueRepository gestionnaireBanqueRepository;

    private final GestionnaireBanqueMapper gestionnaireBanqueMapper;

    public GestionnaireBanqueServiceImpl(
        GestionnaireBanqueRepository gestionnaireBanqueRepository,
        GestionnaireBanqueMapper gestionnaireBanqueMapper
    ) {
        this.gestionnaireBanqueRepository = gestionnaireBanqueRepository;
        this.gestionnaireBanqueMapper = gestionnaireBanqueMapper;
    }

    @Override
    public GestionnaireBanqueDTO save(GestionnaireBanqueDTO gestionnaireBanqueDTO) {
        log.debug("Request to save GestionnaireBanque : {}", gestionnaireBanqueDTO);
        GestionnaireBanque gestionnaireBanque = gestionnaireBanqueMapper.toEntity(gestionnaireBanqueDTO);
        gestionnaireBanque = gestionnaireBanqueRepository.save(gestionnaireBanque);
        return gestionnaireBanqueMapper.toDto(gestionnaireBanque);
    }

    @Override
    public GestionnaireBanqueDTO update(GestionnaireBanqueDTO gestionnaireBanqueDTO) {
        log.debug("Request to update GestionnaireBanque : {}", gestionnaireBanqueDTO);
        GestionnaireBanque gestionnaireBanque = gestionnaireBanqueMapper.toEntity(gestionnaireBanqueDTO);
        gestionnaireBanque = gestionnaireBanqueRepository.save(gestionnaireBanque);
        return gestionnaireBanqueMapper.toDto(gestionnaireBanque);
    }

    @Override
    public Optional<GestionnaireBanqueDTO> partialUpdate(GestionnaireBanqueDTO gestionnaireBanqueDTO) {
        log.debug("Request to partially update GestionnaireBanque : {}", gestionnaireBanqueDTO);

        return gestionnaireBanqueRepository
            .findById(gestionnaireBanqueDTO.getId())
            .map(existingGestionnaireBanque -> {
                gestionnaireBanqueMapper.partialUpdate(existingGestionnaireBanque, gestionnaireBanqueDTO);

                return existingGestionnaireBanque;
            })
            .map(gestionnaireBanqueRepository::save)
            .map(gestionnaireBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GestionnaireBanqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GestionnaireBanques");
        return gestionnaireBanqueRepository.findAll(pageable).map(gestionnaireBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GestionnaireBanqueDTO> findOne(Long id) {
        log.debug("Request to get GestionnaireBanque : {}", id);
        return gestionnaireBanqueRepository.findById(id).map(gestionnaireBanqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GestionnaireBanque : {}", id);
        gestionnaireBanqueRepository.deleteById(id);
    }
}
