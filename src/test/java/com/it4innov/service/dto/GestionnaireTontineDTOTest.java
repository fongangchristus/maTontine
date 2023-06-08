package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GestionnaireTontineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GestionnaireTontineDTO.class);
        GestionnaireTontineDTO gestionnaireTontineDTO1 = new GestionnaireTontineDTO();
        gestionnaireTontineDTO1.setId(1L);
        GestionnaireTontineDTO gestionnaireTontineDTO2 = new GestionnaireTontineDTO();
        assertThat(gestionnaireTontineDTO1).isNotEqualTo(gestionnaireTontineDTO2);
        gestionnaireTontineDTO2.setId(gestionnaireTontineDTO1.getId());
        assertThat(gestionnaireTontineDTO1).isEqualTo(gestionnaireTontineDTO2);
        gestionnaireTontineDTO2.setId(2L);
        assertThat(gestionnaireTontineDTO1).isNotEqualTo(gestionnaireTontineDTO2);
        gestionnaireTontineDTO1.setId(null);
        assertThat(gestionnaireTontineDTO1).isNotEqualTo(gestionnaireTontineDTO2);
    }
}
