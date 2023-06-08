package com.it4innov.service.impl;

import com.it4innov.domain.ContributionPot;
import com.it4innov.repository.ContributionPotRepository;
import com.it4innov.service.ContributionPotService;
import com.it4innov.service.dto.ContributionPotDTO;
import com.it4innov.service.mapper.ContributionPotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContributionPot}.
 */
@Service
@Transactional
public class ContributionPotServiceImpl implements ContributionPotService {

    private final Logger log = LoggerFactory.getLogger(ContributionPotServiceImpl.class);

    private final ContributionPotRepository contributionPotRepository;

    private final ContributionPotMapper contributionPotMapper;

    public ContributionPotServiceImpl(ContributionPotRepository contributionPotRepository, ContributionPotMapper contributionPotMapper) {
        this.contributionPotRepository = contributionPotRepository;
        this.contributionPotMapper = contributionPotMapper;
    }

    @Override
    public ContributionPotDTO save(ContributionPotDTO contributionPotDTO) {
        log.debug("Request to save ContributionPot : {}", contributionPotDTO);
        ContributionPot contributionPot = contributionPotMapper.toEntity(contributionPotDTO);
        contributionPot = contributionPotRepository.save(contributionPot);
        return contributionPotMapper.toDto(contributionPot);
    }

    @Override
    public ContributionPotDTO update(ContributionPotDTO contributionPotDTO) {
        log.debug("Request to update ContributionPot : {}", contributionPotDTO);
        ContributionPot contributionPot = contributionPotMapper.toEntity(contributionPotDTO);
        contributionPot = contributionPotRepository.save(contributionPot);
        return contributionPotMapper.toDto(contributionPot);
    }

    @Override
    public Optional<ContributionPotDTO> partialUpdate(ContributionPotDTO contributionPotDTO) {
        log.debug("Request to partially update ContributionPot : {}", contributionPotDTO);

        return contributionPotRepository
            .findById(contributionPotDTO.getId())
            .map(existingContributionPot -> {
                contributionPotMapper.partialUpdate(existingContributionPot, contributionPotDTO);

                return existingContributionPot;
            })
            .map(contributionPotRepository::save)
            .map(contributionPotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContributionPotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContributionPots");
        return contributionPotRepository.findAll(pageable).map(contributionPotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContributionPotDTO> findOne(Long id) {
        log.debug("Request to get ContributionPot : {}", id);
        return contributionPotRepository.findById(id).map(contributionPotMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContributionPot : {}", id);
        contributionPotRepository.deleteById(id);
    }
}
