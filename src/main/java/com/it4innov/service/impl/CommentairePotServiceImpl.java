package com.it4innov.service.impl;

import com.it4innov.domain.CommentairePot;
import com.it4innov.repository.CommentairePotRepository;
import com.it4innov.service.CommentairePotService;
import com.it4innov.service.dto.CommentairePotDTO;
import com.it4innov.service.mapper.CommentairePotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommentairePot}.
 */
@Service
@Transactional
public class CommentairePotServiceImpl implements CommentairePotService {

    private final Logger log = LoggerFactory.getLogger(CommentairePotServiceImpl.class);

    private final CommentairePotRepository commentairePotRepository;

    private final CommentairePotMapper commentairePotMapper;

    public CommentairePotServiceImpl(CommentairePotRepository commentairePotRepository, CommentairePotMapper commentairePotMapper) {
        this.commentairePotRepository = commentairePotRepository;
        this.commentairePotMapper = commentairePotMapper;
    }

    @Override
    public CommentairePotDTO save(CommentairePotDTO commentairePotDTO) {
        log.debug("Request to save CommentairePot : {}", commentairePotDTO);
        CommentairePot commentairePot = commentairePotMapper.toEntity(commentairePotDTO);
        commentairePot = commentairePotRepository.save(commentairePot);
        return commentairePotMapper.toDto(commentairePot);
    }

    @Override
    public CommentairePotDTO update(CommentairePotDTO commentairePotDTO) {
        log.debug("Request to update CommentairePot : {}", commentairePotDTO);
        CommentairePot commentairePot = commentairePotMapper.toEntity(commentairePotDTO);
        commentairePot = commentairePotRepository.save(commentairePot);
        return commentairePotMapper.toDto(commentairePot);
    }

    @Override
    public Optional<CommentairePotDTO> partialUpdate(CommentairePotDTO commentairePotDTO) {
        log.debug("Request to partially update CommentairePot : {}", commentairePotDTO);

        return commentairePotRepository
            .findById(commentairePotDTO.getId())
            .map(existingCommentairePot -> {
                commentairePotMapper.partialUpdate(existingCommentairePot, commentairePotDTO);

                return existingCommentairePot;
            })
            .map(commentairePotRepository::save)
            .map(commentairePotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentairePotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommentairePots");
        return commentairePotRepository.findAll(pageable).map(commentairePotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentairePotDTO> findOne(Long id) {
        log.debug("Request to get CommentairePot : {}", id);
        return commentairePotRepository.findById(id).map(commentairePotMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommentairePot : {}", id);
        commentairePotRepository.deleteById(id);
    }
}
