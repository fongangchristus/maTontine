package com.it4innov.service.impl;

import com.it4innov.domain.HistoriquePersonne;
import com.it4innov.repository.HistoriquePersonneRepository;
import com.it4innov.service.HistoriquePersonneService;
import com.it4innov.service.dto.HistoriquePersonneDTO;
import com.it4innov.service.mapper.HistoriquePersonneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HistoriquePersonne}.
 */
@Service
@Transactional
public class HistoriquePersonneServiceImpl implements HistoriquePersonneService {

    private final Logger log = LoggerFactory.getLogger(HistoriquePersonneServiceImpl.class);

    private final HistoriquePersonneRepository historiquePersonneRepository;

    private final HistoriquePersonneMapper historiquePersonneMapper;

    public HistoriquePersonneServiceImpl(
        HistoriquePersonneRepository historiquePersonneRepository,
        HistoriquePersonneMapper historiquePersonneMapper
    ) {
        this.historiquePersonneRepository = historiquePersonneRepository;
        this.historiquePersonneMapper = historiquePersonneMapper;
    }

    @Override
    public HistoriquePersonneDTO save(HistoriquePersonneDTO historiquePersonneDTO) {
        log.debug("Request to save HistoriquePersonne : {}", historiquePersonneDTO);
        HistoriquePersonne historiquePersonne = historiquePersonneMapper.toEntity(historiquePersonneDTO);
        historiquePersonne = historiquePersonneRepository.save(historiquePersonne);
        return historiquePersonneMapper.toDto(historiquePersonne);
    }

    @Override
    public HistoriquePersonneDTO update(HistoriquePersonneDTO historiquePersonneDTO) {
        log.debug("Request to update HistoriquePersonne : {}", historiquePersonneDTO);
        HistoriquePersonne historiquePersonne = historiquePersonneMapper.toEntity(historiquePersonneDTO);
        historiquePersonne = historiquePersonneRepository.save(historiquePersonne);
        return historiquePersonneMapper.toDto(historiquePersonne);
    }

    @Override
    public Optional<HistoriquePersonneDTO> partialUpdate(HistoriquePersonneDTO historiquePersonneDTO) {
        log.debug("Request to partially update HistoriquePersonne : {}", historiquePersonneDTO);

        return historiquePersonneRepository
            .findById(historiquePersonneDTO.getId())
            .map(existingHistoriquePersonne -> {
                historiquePersonneMapper.partialUpdate(existingHistoriquePersonne, historiquePersonneDTO);

                return existingHistoriquePersonne;
            })
            .map(historiquePersonneRepository::save)
            .map(historiquePersonneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriquePersonneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriquePersonnes");
        return historiquePersonneRepository.findAll(pageable).map(historiquePersonneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriquePersonneDTO> findOne(Long id) {
        log.debug("Request to get HistoriquePersonne : {}", id);
        return historiquePersonneRepository.findById(id).map(historiquePersonneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoriquePersonne : {}", id);
        historiquePersonneRepository.deleteById(id);
    }
}
