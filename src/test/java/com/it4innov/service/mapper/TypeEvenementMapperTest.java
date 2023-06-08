package com.it4innov.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeEvenementMapperTest {

    private TypeEvenementMapper typeEvenementMapper;

    @BeforeEach
    public void setUp() {
        typeEvenementMapper = new TypeEvenementMapperImpl();
    }
}
