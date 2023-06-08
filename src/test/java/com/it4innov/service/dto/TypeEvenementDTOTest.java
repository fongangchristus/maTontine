package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeEvenementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeEvenementDTO.class);
        TypeEvenementDTO typeEvenementDTO1 = new TypeEvenementDTO();
        typeEvenementDTO1.setId(1L);
        TypeEvenementDTO typeEvenementDTO2 = new TypeEvenementDTO();
        assertThat(typeEvenementDTO1).isNotEqualTo(typeEvenementDTO2);
        typeEvenementDTO2.setId(typeEvenementDTO1.getId());
        assertThat(typeEvenementDTO1).isEqualTo(typeEvenementDTO2);
        typeEvenementDTO2.setId(2L);
        assertThat(typeEvenementDTO1).isNotEqualTo(typeEvenementDTO2);
        typeEvenementDTO1.setId(null);
        assertThat(typeEvenementDTO1).isNotEqualTo(typeEvenementDTO2);
    }
}
