package com.it4innov.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssembleMapperTest {

    private AssembleMapper assembleMapper;

    @BeforeEach
    public void setUp() {
        assembleMapper = new AssembleMapperImpl();
    }
}
