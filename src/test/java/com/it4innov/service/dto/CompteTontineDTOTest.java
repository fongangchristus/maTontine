package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteTontineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteTontineDTO.class);
        CompteTontineDTO compteTontineDTO1 = new CompteTontineDTO();
        compteTontineDTO1.setId(1L);
        CompteTontineDTO compteTontineDTO2 = new CompteTontineDTO();
        assertThat(compteTontineDTO1).isNotEqualTo(compteTontineDTO2);
        compteTontineDTO2.setId(compteTontineDTO1.getId());
        assertThat(compteTontineDTO1).isEqualTo(compteTontineDTO2);
        compteTontineDTO2.setId(2L);
        assertThat(compteTontineDTO1).isNotEqualTo(compteTontineDTO2);
        compteTontineDTO1.setId(null);
        assertThat(compteTontineDTO1).isNotEqualTo(compteTontineDTO2);
    }
}
