package com.it4innov.service.impl;

import com.it4innov.domain.FonctionAdherent;
import com.it4innov.repository.FonctionAdherentRepository;
import com.it4innov.service.FonctionAdherentService;
import com.it4innov.service.dto.FonctionAdherentDTO;
import com.it4innov.service.mapper.FonctionAdherentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FonctionAdherent}.
 */
@Service
@Transactional
public class FonctionAdherentServiceImpl implements FonctionAdherentService {

    private final Logger log = LoggerFactory.getLogger(FonctionAdherentServiceImpl.class);

    private final FonctionAdherentRepository fonctionAdherentRepository;

    private final FonctionAdherentMapper fonctionAdherentMapper;

    public FonctionAdherentServiceImpl(
        FonctionAdherentRepository fonctionAdherentRepository,
        FonctionAdherentMapper fonctionAdherentMapper
    ) {
        this.fonctionAdherentRepository = fonctionAdherentRepository;
        this.fonctionAdherentMapper = fonctionAdherentMapper;
    }

    @Override
    public FonctionAdherentDTO save(FonctionAdherentDTO fonctionAdherentDTO) {
        log.debug("Request to save FonctionAdherent : {}", fonctionAdherentDTO);
        FonctionAdherent fonctionAdherent = fonctionAdherentMapper.toEntity(fonctionAdherentDTO);
        fonctionAdherent = fonctionAdherentRepository.save(fonctionAdherent);
        return fonctionAdherentMapper.toDto(fonctionAdherent);
    }

    @Override
    public FonctionAdherentDTO update(FonctionAdherentDTO fonctionAdherentDTO) {
        log.debug("Request to update FonctionAdherent : {}", fonctionAdherentDTO);
        FonctionAdherent fonctionAdherent = fonctionAdherentMapper.toEntity(fonctionAdherentDTO);
        fonctionAdherent = fonctionAdherentRepository.save(fonctionAdherent);
        return fonctionAdherentMapper.toDto(fonctionAdherent);
    }

    @Override
    public Optional<FonctionAdherentDTO> partialUpdate(FonctionAdherentDTO fonctionAdherentDTO) {
        log.debug("Request to partially update FonctionAdherent : {}", fonctionAdherentDTO);

        return fonctionAdherentRepository
            .findById(fonctionAdherentDTO.getId())
            .map(existingFonctionAdherent -> {
                fonctionAdherentMapper.partialUpdate(existingFonctionAdherent, fonctionAdherentDTO);

                return existingFonctionAdherent;
            })
            .map(fonctionAdherentRepository::save)
            .map(fonctionAdherentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FonctionAdherentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FonctionAdherents");
        return fonctionAdherentRepository.findAll(pageable).map(fonctionAdherentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FonctionAdherentDTO> findOne(Long id) {
        log.debug("Request to get FonctionAdherent : {}", id);
        return fonctionAdherentRepository.findById(id).map(fonctionAdherentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FonctionAdherent : {}", id);
        fonctionAdherentRepository.deleteById(id);
    }
}
