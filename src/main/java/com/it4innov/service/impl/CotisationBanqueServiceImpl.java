package com.it4innov.service.impl;

import com.it4innov.domain.CotisationBanque;
import com.it4innov.repository.CotisationBanqueRepository;
import com.it4innov.service.CotisationBanqueService;
import com.it4innov.service.dto.CotisationBanqueDTO;
import com.it4innov.service.mapper.CotisationBanqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CotisationBanque}.
 */
@Service
@Transactional
public class CotisationBanqueServiceImpl implements CotisationBanqueService {

    private final Logger log = LoggerFactory.getLogger(CotisationBanqueServiceImpl.class);

    private final CotisationBanqueRepository cotisationBanqueRepository;

    private final CotisationBanqueMapper cotisationBanqueMapper;

    public CotisationBanqueServiceImpl(
        CotisationBanqueRepository cotisationBanqueRepository,
        CotisationBanqueMapper cotisationBanqueMapper
    ) {
        this.cotisationBanqueRepository = cotisationBanqueRepository;
        this.cotisationBanqueMapper = cotisationBanqueMapper;
    }

    @Override
    public CotisationBanqueDTO save(CotisationBanqueDTO cotisationBanqueDTO) {
        log.debug("Request to save CotisationBanque : {}", cotisationBanqueDTO);
        CotisationBanque cotisationBanque = cotisationBanqueMapper.toEntity(cotisationBanqueDTO);
        cotisationBanque = cotisationBanqueRepository.save(cotisationBanque);
        return cotisationBanqueMapper.toDto(cotisationBanque);
    }

    @Override
    public CotisationBanqueDTO update(CotisationBanqueDTO cotisationBanqueDTO) {
        log.debug("Request to update CotisationBanque : {}", cotisationBanqueDTO);
        CotisationBanque cotisationBanque = cotisationBanqueMapper.toEntity(cotisationBanqueDTO);
        cotisationBanque = cotisationBanqueRepository.save(cotisationBanque);
        return cotisationBanqueMapper.toDto(cotisationBanque);
    }

    @Override
    public Optional<CotisationBanqueDTO> partialUpdate(CotisationBanqueDTO cotisationBanqueDTO) {
        log.debug("Request to partially update CotisationBanque : {}", cotisationBanqueDTO);

        return cotisationBanqueRepository
            .findById(cotisationBanqueDTO.getId())
            .map(existingCotisationBanque -> {
                cotisationBanqueMapper.partialUpdate(existingCotisationBanque, cotisationBanqueDTO);

                return existingCotisationBanque;
            })
            .map(cotisationBanqueRepository::save)
            .map(cotisationBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CotisationBanqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CotisationBanques");
        return cotisationBanqueRepository.findAll(pageable).map(cotisationBanqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CotisationBanqueDTO> findOne(Long id) {
        log.debug("Request to get CotisationBanque : {}", id);
        return cotisationBanqueRepository.findById(id).map(cotisationBanqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CotisationBanque : {}", id);
        cotisationBanqueRepository.deleteById(id);
    }
}
