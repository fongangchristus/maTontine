package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PotDTO.class);
        PotDTO potDTO1 = new PotDTO();
        potDTO1.setId(1L);
        PotDTO potDTO2 = new PotDTO();
        assertThat(potDTO1).isNotEqualTo(potDTO2);
        potDTO2.setId(potDTO1.getId());
        assertThat(potDTO1).isEqualTo(potDTO2);
        potDTO2.setId(2L);
        assertThat(potDTO1).isNotEqualTo(potDTO2);
        potDTO1.setId(null);
        assertThat(potDTO1).isNotEqualTo(potDTO2);
    }
}
