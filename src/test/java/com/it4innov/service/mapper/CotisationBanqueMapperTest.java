package com.it4innov.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CotisationBanqueMapperTest {

    private CotisationBanqueMapper cotisationBanqueMapper;

    @BeforeEach
    public void setUp() {
        cotisationBanqueMapper = new CotisationBanqueMapperImpl();
    }
}
