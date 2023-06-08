package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePotDTO.class);
        TypePotDTO typePotDTO1 = new TypePotDTO();
        typePotDTO1.setId(1L);
        TypePotDTO typePotDTO2 = new TypePotDTO();
        assertThat(typePotDTO1).isNotEqualTo(typePotDTO2);
        typePotDTO2.setId(typePotDTO1.getId());
        assertThat(typePotDTO1).isEqualTo(typePotDTO2);
        typePotDTO2.setId(2L);
        assertThat(typePotDTO1).isNotEqualTo(typePotDTO2);
        typePotDTO1.setId(null);
        assertThat(typePotDTO1).isNotEqualTo(typePotDTO2);
    }
}
