package com.it4innov.service.impl;

import com.it4innov.domain.DocumentAssociation;
import com.it4innov.repository.DocumentAssociationRepository;
import com.it4innov.service.DocumentAssociationService;
import com.it4innov.service.dto.DocumentAssociationDTO;
import com.it4innov.service.mapper.DocumentAssociationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentAssociation}.
 */
@Service
@Transactional
public class DocumentAssociationServiceImpl implements DocumentAssociationService {

    private final Logger log = LoggerFactory.getLogger(DocumentAssociationServiceImpl.class);

    private final DocumentAssociationRepository documentAssociationRepository;

    private final DocumentAssociationMapper documentAssociationMapper;

    public DocumentAssociationServiceImpl(
        DocumentAssociationRepository documentAssociationRepository,
        DocumentAssociationMapper documentAssociationMapper
    ) {
        this.documentAssociationRepository = documentAssociationRepository;
        this.documentAssociationMapper = documentAssociationMapper;
    }

    @Override
    public DocumentAssociationDTO save(DocumentAssociationDTO documentAssociationDTO) {
        log.debug("Request to save DocumentAssociation : {}", documentAssociationDTO);
        DocumentAssociation documentAssociation = documentAssociationMapper.toEntity(documentAssociationDTO);
        documentAssociation = documentAssociationRepository.save(documentAssociation);
        return documentAssociationMapper.toDto(documentAssociation);
    }

    @Override
    public DocumentAssociationDTO update(DocumentAssociationDTO documentAssociationDTO) {
        log.debug("Request to update DocumentAssociation : {}", documentAssociationDTO);
        DocumentAssociation documentAssociation = documentAssociationMapper.toEntity(documentAssociationDTO);
        documentAssociation = documentAssociationRepository.save(documentAssociation);
        return documentAssociationMapper.toDto(documentAssociation);
    }

    @Override
    public Optional<DocumentAssociationDTO> partialUpdate(DocumentAssociationDTO documentAssociationDTO) {
        log.debug("Request to partially update DocumentAssociation : {}", documentAssociationDTO);

        return documentAssociationRepository
            .findById(documentAssociationDTO.getId())
            .map(existingDocumentAssociation -> {
                documentAssociationMapper.partialUpdate(existingDocumentAssociation, documentAssociationDTO);

                return existingDocumentAssociation;
            })
            .map(documentAssociationRepository::save)
            .map(documentAssociationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentAssociationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentAssociations");
        return documentAssociationRepository.findAll(pageable).map(documentAssociationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentAssociationDTO> findOne(Long id) {
        log.debug("Request to get DocumentAssociation : {}", id);
        return documentAssociationRepository.findById(id).map(documentAssociationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentAssociation : {}", id);
        documentAssociationRepository.deleteById(id);
    }
}
