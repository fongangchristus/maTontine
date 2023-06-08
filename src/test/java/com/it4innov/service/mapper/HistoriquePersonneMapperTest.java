package com.it4innov.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriquePersonneMapperTest {

    private HistoriquePersonneMapper historiquePersonneMapper;

    @BeforeEach
    public void setUp() {
        historiquePersonneMapper = new HistoriquePersonneMapperImpl();
    }
}
