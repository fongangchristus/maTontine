package com.it4innov.service.impl;

import com.it4innov.domain.Personne;
import com.it4innov.repository.PersonneRepository;
import com.it4innov.service.PersonneService;
import com.it4innov.service.dto.PersonneDTO;
import com.it4innov.service.mapper.PersonneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Personne}.
 */
@Service
@Transactional
public class PersonneServiceImpl implements PersonneService {

    private final Logger log = LoggerFactory.getLogger(PersonneServiceImpl.class);

    private final PersonneRepository personneRepository;

    private final PersonneMapper personneMapper;

    public PersonneServiceImpl(PersonneRepository personneRepository, PersonneMapper personneMapper) {
        this.personneRepository = personneRepository;
        this.personneMapper = personneMapper;
    }

    @Override
    public PersonneDTO save(PersonneDTO personneDTO) {
        log.debug("Request to save Personne : {}", personneDTO);
        Personne personne = personneMapper.toEntity(personneDTO);
        personne = personneRepository.save(personne);
        return personneMapper.toDto(personne);
    }

    @Override
    public PersonneDTO update(PersonneDTO personneDTO) {
        log.debug("Request to update Personne : {}", personneDTO);
        Personne personne = personneMapper.toEntity(personneDTO);
        personne = personneRepository.save(personne);
        return personneMapper.toDto(personne);
    }

    @Override
    public Optional<PersonneDTO> partialUpdate(PersonneDTO personneDTO) {
        log.debug("Request to partially update Personne : {}", personneDTO);

        return personneRepository
            .findById(personneDTO.getId())
            .map(existingPersonne -> {
                personneMapper.partialUpdate(existingPersonne, personneDTO);

                return existingPersonne;
            })
            .map(personneRepository::save)
            .map(personneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Personnes");
        return personneRepository.findAll(pageable).map(personneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonneDTO> findOne(Long id) {
        log.debug("Request to get Personne : {}", id);
        return personneRepository.findById(id).map(personneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personne : {}", id);
        personneRepository.deleteById(id);
    }
}
