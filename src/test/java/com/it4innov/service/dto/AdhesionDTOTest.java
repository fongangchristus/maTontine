package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdhesionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdhesionDTO.class);
        AdhesionDTO adhesionDTO1 = new AdhesionDTO();
        adhesionDTO1.setId(1L);
        AdhesionDTO adhesionDTO2 = new AdhesionDTO();
        assertThat(adhesionDTO1).isNotEqualTo(adhesionDTO2);
        adhesionDTO2.setId(adhesionDTO1.getId());
        assertThat(adhesionDTO1).isEqualTo(adhesionDTO2);
        adhesionDTO2.setId(2L);
        assertThat(adhesionDTO1).isNotEqualTo(adhesionDTO2);
        adhesionDTO1.setId(null);
        assertThat(adhesionDTO1).isNotEqualTo(adhesionDTO2);
    }
}
