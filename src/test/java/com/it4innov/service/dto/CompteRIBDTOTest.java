package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteRIBDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteRIBDTO.class);
        CompteRIBDTO compteRIBDTO1 = new CompteRIBDTO();
        compteRIBDTO1.setId(1L);
        CompteRIBDTO compteRIBDTO2 = new CompteRIBDTO();
        assertThat(compteRIBDTO1).isNotEqualTo(compteRIBDTO2);
        compteRIBDTO2.setId(compteRIBDTO1.getId());
        assertThat(compteRIBDTO1).isEqualTo(compteRIBDTO2);
        compteRIBDTO2.setId(2L);
        assertThat(compteRIBDTO1).isNotEqualTo(compteRIBDTO2);
        compteRIBDTO1.setId(null);
        assertThat(compteRIBDTO1).isNotEqualTo(compteRIBDTO2);
    }
}
