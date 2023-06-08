package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonnaieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonnaieDTO.class);
        MonnaieDTO monnaieDTO1 = new MonnaieDTO();
        monnaieDTO1.setId(1L);
        MonnaieDTO monnaieDTO2 = new MonnaieDTO();
        assertThat(monnaieDTO1).isNotEqualTo(monnaieDTO2);
        monnaieDTO2.setId(monnaieDTO1.getId());
        assertThat(monnaieDTO1).isEqualTo(monnaieDTO2);
        monnaieDTO2.setId(2L);
        assertThat(monnaieDTO1).isNotEqualTo(monnaieDTO2);
        monnaieDTO1.setId(null);
        assertThat(monnaieDTO1).isNotEqualTo(monnaieDTO2);
    }
}
