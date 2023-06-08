package com.it4innov.service.impl;

import com.it4innov.domain.DecaisementBanque;
import com.it4innov.repository.DecaisementBanqueRepository;
import com.it4innov.service.DecaisementBanqueService;
import com.it4innov.service.dto.DecaisementBanqueDTO;
import com.it4innov.service.mapper.DecaisementBanqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DecaisementBanque}.
 */
@Service
@Transactional
public class DecaisementBanqueServiceImpl implements DecaisementBanqueService {

    private final Logger log = LoggerFactory.getLogger(DecaisementBanqueServiceImpl.class);

    private final DecaisementBanqueRepository decaisementBanqueRepository;

    private final DecaisementBanqueMapper decaisementBanqueMapper;

    public DecaisementBanqueServiceImpl(
        DecaisementBanqueRepository decaisementBanqueRepository,
        DecaisementBanqueMapper decaisementBanqueMapper
    ) {
        this.decaisementBanqueRepository = decaisementBanqueRepository;
        this.decaisementBanqueMapper = decaisementBanqueMapper;
    }

    @Override
    public DecaisementBanqueDTO save(DecaisementBanqueDTO decaisementBanqueDTO) {
        log.debug("Request to save DecaisementBanque : {}", decaisementBanqueDTO);
        DecaisementBanque decaisementBanque = decaisementBanqueMapper.toEntity(decaisementBanqueDTO);
        decaisementBanque = decaisementBanqueRepository.save(decaisementBanque);
        return decaisementBanqueMapper.toDto(decaisementBanque);
    }

    @Override
    public DecaisementBanqueDTO update(DecaisementBanqueDTO decaisementBanqueDTO) {
        log.debug("Request to update DecaisementBanque : {}", decaisementBanqueDTO);
        DecaisementBanque decaisementBanque = decaisementBanqueMapper.toEntity(decaisementBanqueDTO);
        decaisementBanque = decaisementBanqueRepository.save(decaisementBanque);
        return decaisementBanqueMapper.toDto(decaisementBanque);
    }

    @Override
    public Optional<DecaisementBanqueDTO> partialUpdate(DecaisementBanqueDTO decaisementBanqueDTO) {
        log.debug("Request to partially update DecaisementBanque : {}", decaisementBanqueDTO);

        return decaisementBanqueRepository
            .findById(decaisementBanqueDTO.getId())
            .map(existingDecaisementBanque -> {
                decaisementBanqueMapper.partialUpdate(existingDecaisementBanque, decaisementBanqueDTO);

                return existingDecaisementBanque;
            })
            .map(decaisementBanqueRepository::save)
            .map(decaisementBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DecaisementBanqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DecaisementBanques");
        return decaisementBanqueRepository.findAll(pageable).map(decaisementBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DecaisementBanqueDTO> findOne(Long id) {
        log.debug("Request to get DecaisementBanque : {}", id);
        return decaisementBanqueRepository.findById(id).map(decaisementBanqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DecaisementBanque : {}", id);
        decaisementBanqueRepository.deleteById(id);
    }
}
