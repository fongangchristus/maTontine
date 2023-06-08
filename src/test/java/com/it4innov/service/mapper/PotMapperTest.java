package com.it4innov.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PotMapperTest {

    private PotMapper potMapper;

    @BeforeEach
    public void setUp() {
        potMapper = new PotMapperImpl();
    }
}
