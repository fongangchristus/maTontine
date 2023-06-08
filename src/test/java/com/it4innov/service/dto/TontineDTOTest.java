package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TontineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TontineDTO.class);
        TontineDTO tontineDTO1 = new TontineDTO();
        tontineDTO1.setId(1L);
        TontineDTO tontineDTO2 = new TontineDTO();
        assertThat(tontineDTO1).isNotEqualTo(tontineDTO2);
        tontineDTO2.setId(tontineDTO1.getId());
        assertThat(tontineDTO1).isEqualTo(tontineDTO2);
        tontineDTO2.setId(2L);
        assertThat(tontineDTO1).isNotEqualTo(tontineDTO2);
        tontineDTO1.setId(null);
        assertThat(tontineDTO1).isNotEqualTo(tontineDTO2);
    }
}
