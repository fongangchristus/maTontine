package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteBanqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteBanqueDTO.class);
        CompteBanqueDTO compteBanqueDTO1 = new CompteBanqueDTO();
        compteBanqueDTO1.setId(1L);
        CompteBanqueDTO compteBanqueDTO2 = new CompteBanqueDTO();
        assertThat(compteBanqueDTO1).isNotEqualTo(compteBanqueDTO2);
        compteBanqueDTO2.setId(compteBanqueDTO1.getId());
        assertThat(compteBanqueDTO1).isEqualTo(compteBanqueDTO2);
        compteBanqueDTO2.setId(2L);
        assertThat(compteBanqueDTO1).isNotEqualTo(compteBanqueDTO2);
        compteBanqueDTO1.setId(null);
        assertThat(compteBanqueDTO1).isNotEqualTo(compteBanqueDTO2);
    }
}
