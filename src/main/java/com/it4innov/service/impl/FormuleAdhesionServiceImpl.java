package com.it4innov.service.impl;

import com.it4innov.domain.FormuleAdhesion;
import com.it4innov.repository.FormuleAdhesionRepository;
import com.it4innov.service.FormuleAdhesionService;
import com.it4innov.service.dto.FormuleAdhesionDTO;
import com.it4innov.service.mapper.FormuleAdhesionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FormuleAdhesion}.
 */
@Service
@Transactional
public class FormuleAdhesionServiceImpl implements FormuleAdhesionService {

    private final Logger log = LoggerFactory.getLogger(FormuleAdhesionServiceImpl.class);

    private final FormuleAdhesionRepository formuleAdhesionRepository;

    private final FormuleAdhesionMapper formuleAdhesionMapper;

    public FormuleAdhesionServiceImpl(FormuleAdhesionRepository formuleAdhesionRepository, FormuleAdhesionMapper formuleAdhesionMapper) {
        this.formuleAdhesionRepository = formuleAdhesionRepository;
        this.formuleAdhesionMapper = formuleAdhesionMapper;
    }

    @Override
    public FormuleAdhesionDTO save(FormuleAdhesionDTO formuleAdhesionDTO) {
        log.debug("Request to save FormuleAdhesion : {}", formuleAdhesionDTO);
        FormuleAdhesion formuleAdhesion = formuleAdhesionMapper.toEntity(formuleAdhesionDTO);
        formuleAdhesion = formuleAdhesionRepository.save(formuleAdhesion);
        return formuleAdhesionMapper.toDto(formuleAdhesion);
    }

    @Override
    public FormuleAdhesionDTO update(FormuleAdhesionDTO formuleAdhesionDTO) {
        log.debug("Request to update FormuleAdhesion : {}", formuleAdhesionDTO);
        FormuleAdhesion formuleAdhesion = formuleAdhesionMapper.toEntity(formuleAdhesionDTO);
        formuleAdhesion = formuleAdhesionRepository.save(formuleAdhesion);
        return formuleAdhesionMapper.toDto(formuleAdhesion);
    }

    @Override
    public Optional<FormuleAdhesionDTO> partialUpdate(FormuleAdhesionDTO formuleAdhesionDTO) {
        log.debug("Request to partially update FormuleAdhesion : {}", formuleAdhesionDTO);

        return formuleAdhesionRepository
            .findById(formuleAdhesionDTO.getId())
            .map(existingFormuleAdhesion -> {
                formuleAdhesionMapper.partialUpdate(existingFormuleAdhesion, formuleAdhesionDTO);

                return existingFormuleAdhesion;
            })
            .map(formuleAdhesionRepository::save)
            .map(formuleAdhesionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormuleAdhesionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormuleAdhesions");
        return formuleAdhesionRepository.findAll(pageable).map(formuleAdhesionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormuleAdhesionDTO> findOne(Long id) {
        log.debug("Request to get FormuleAdhesion : {}", id);
        return formuleAdhesionRepository.findById(id).map(formuleAdhesionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormuleAdhesion : {}", id);
        formuleAdhesionRepository.deleteById(id);
    }
}
