package com.it4innov.service.impl;

import com.it4innov.domain.Banque;
import com.it4innov.repository.BanqueRepository;
import com.it4innov.service.BanqueService;
import com.it4innov.service.dto.BanqueDTO;
import com.it4innov.service.mapper.BanqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Banque}.
 */
@Service
@Transactional
public class BanqueServiceImpl implements BanqueService {

    private final Logger log = LoggerFactory.getLogger(BanqueServiceImpl.class);

    private final BanqueRepository banqueRepository;

    private final BanqueMapper banqueMapper;

    public BanqueServiceImpl(BanqueRepository banqueRepository, BanqueMapper banqueMapper) {
        this.banqueRepository = banqueRepository;
        this.banqueMapper = banqueMapper;
    }

    @Override
    public BanqueDTO save(BanqueDTO banqueDTO) {
        log.debug("Request to save Banque : {}", banqueDTO);
        Banque banque = banqueMapper.toEntity(banqueDTO);
        banque = banqueRepository.save(banque);
        return banqueMapper.toDto(banque);
    }

    @Override
    public BanqueDTO update(BanqueDTO banqueDTO) {
        log.debug("Request to update Banque : {}", banqueDTO);
        Banque banque = banqueMapper.toEntity(banqueDTO);
        banque = banqueRepository.save(banque);
        return banqueMapper.toDto(banque);
    }

    @Override
    public Optional<BanqueDTO> partialUpdate(BanqueDTO banqueDTO) {
        log.debug("Request to partially update Banque : {}", banqueDTO);

        return banqueRepository
            .findById(banqueDTO.getId())
            .map(existingBanque -> {
                banqueMapper.partialUpdate(existingBanque, banqueDTO);

                return existingBanque;
            })
            .map(banqueRepository::save)
            .map(banqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BanqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Banques");
        return banqueRepository.findAll(pageable).map(banqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BanqueDTO> findOne(Long id) {
        log.debug("Request to get Banque : {}", id);
        return banqueRepository.findById(id).map(banqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Banque : {}", id);
        banqueRepository.deleteById(id);
    }
}
