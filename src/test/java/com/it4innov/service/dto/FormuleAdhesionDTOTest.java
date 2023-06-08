package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormuleAdhesionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormuleAdhesionDTO.class);
        FormuleAdhesionDTO formuleAdhesionDTO1 = new FormuleAdhesionDTO();
        formuleAdhesionDTO1.setId(1L);
        FormuleAdhesionDTO formuleAdhesionDTO2 = new FormuleAdhesionDTO();
        assertThat(formuleAdhesionDTO1).isNotEqualTo(formuleAdhesionDTO2);
        formuleAdhesionDTO2.setId(formuleAdhesionDTO1.getId());
        assertThat(formuleAdhesionDTO1).isEqualTo(formuleAdhesionDTO2);
        formuleAdhesionDTO2.setId(2L);
        assertThat(formuleAdhesionDTO1).isNotEqualTo(formuleAdhesionDTO2);
        formuleAdhesionDTO1.setId(null);
        assertThat(formuleAdhesionDTO1).isNotEqualTo(formuleAdhesionDTO2);
    }
}
