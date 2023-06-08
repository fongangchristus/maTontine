package com.it4innov.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SanctionConfigurationMapperTest {

    private SanctionConfigurationMapper sanctionConfigurationMapper;

    @BeforeEach
    public void setUp() {
        sanctionConfigurationMapper = new SanctionConfigurationMapperImpl();
    }
}
