package com.it4innov.service.impl;

import com.it4innov.domain.SessionTontine;
import com.it4innov.repository.SessionTontineRepository;
import com.it4innov.service.SessionTontineService;
import com.it4innov.service.dto.SessionTontineDTO;
import com.it4innov.service.mapper.SessionTontineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SessionTontine}.
 */
@Service
@Transactional
public class SessionTontineServiceImpl implements SessionTontineService {

    private final Logger log = LoggerFactory.getLogger(SessionTontineServiceImpl.class);

    private final SessionTontineRepository sessionTontineRepository;

    private final SessionTontineMapper sessionTontineMapper;

    public SessionTontineServiceImpl(SessionTontineRepository sessionTontineRepository, SessionTontineMapper sessionTontineMapper) {
        this.sessionTontineRepository = sessionTontineRepository;
        this.sessionTontineMapper = sessionTontineMapper;
    }

    @Override
    public SessionTontineDTO save(SessionTontineDTO sessionTontineDTO) {
        log.debug("Request to save SessionTontine : {}", sessionTontineDTO);
        SessionTontine sessionTontine = sessionTontineMapper.toEntity(sessionTontineDTO);
        sessionTontine = sessionTontineRepository.save(sessionTontine);
        return sessionTontineMapper.toDto(sessionTontine);
    }

    @Override
    public SessionTontineDTO update(SessionTontineDTO sessionTontineDTO) {
        log.debug("Request to update SessionTontine : {}", sessionTontineDTO);
        SessionTontine sessionTontine = sessionTontineMapper.toEntity(sessionTontineDTO);
        sessionTontine = sessionTontineRepository.save(sessionTontine);
        return sessionTontineMapper.toDto(sessionTontine);
    }

    @Override
    public Optional<SessionTontineDTO> partialUpdate(SessionTontineDTO sessionTontineDTO) {
        log.debug("Request to partially update SessionTontine : {}", sessionTontineDTO);

        return sessionTontineRepository
            .findById(sessionTontineDTO.getId())
            .map(existingSessionTontine -> {
                sessionTontineMapper.partialUpdate(existingSessionTontine, sessionTontineDTO);

                return existingSessionTontine;
            })
            .map(sessionTontineRepository::save)
            .map(sessionTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SessionTontineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SessionTontines");
        return sessionTontineRepository.findAll(pageable).map(sessionTontineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionTontineDTO> findOne(Long id) {
        log.debug("Request to get SessionTontine : {}", id);
        return sessionTontineRepository.findById(id).map(sessionTontineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SessionTontine : {}", id);
        sessionTontineRepository.deleteById(id);
    }
}
