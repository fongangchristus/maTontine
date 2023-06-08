package com.it4innov.service.impl;

import com.it4innov.domain.Association;
import com.it4innov.repository.AssociationRepository;
import com.it4innov.service.AssociationService;
import com.it4innov.service.dto.AssociationDTO;
import com.it4innov.service.mapper.AssociationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Association}.
 */
@Service
@Transactional
public class AssociationServiceImpl implements AssociationService {

    private final Logger log = LoggerFactory.getLogger(AssociationServiceImpl.class);

    private final AssociationRepository associationRepository;

    private final AssociationMapper associationMapper;

    public AssociationServiceImpl(AssociationRepository associationRepository, AssociationMapper associationMapper) {
        this.associationRepository = associationRepository;
        this.associationMapper = associationMapper;
    }

    @Override
    public AssociationDTO save(AssociationDTO associationDTO) {
        log.debug("Request to save Association : {}", associationDTO);
        Association association = associationMapper.toEntity(associationDTO);
        association = associationRepository.save(association);
        return associationMapper.toDto(association);
    }

    @Override
    public AssociationDTO update(AssociationDTO associationDTO) {
        log.debug("Request to update Association : {}", associationDTO);
        Association association = associationMapper.toEntity(associationDTO);
        association = associationRepository.save(association);
        return associationMapper.toDto(association);
    }

    @Override
    public Optional<AssociationDTO> partialUpdate(AssociationDTO associationDTO) {
        log.debug("Request to partially update Association : {}", associationDTO);

        return associationRepository
            .findById(associationDTO.getId())
            .map(existingAssociation -> {
                associationMapper.partialUpdate(existingAssociation, associationDTO);

                return existingAssociation;
            })
            .map(associationRepository::save)
            .map(associationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssociationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Associations");
        return associationRepository.findAll(pageable).map(associationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssociationDTO> findOne(Long id) {
        log.debug("Request to get Association : {}", id);
        return associationRepository.findById(id).map(associationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Association : {}", id);
        associationRepository.deleteById(id);
    }
}
