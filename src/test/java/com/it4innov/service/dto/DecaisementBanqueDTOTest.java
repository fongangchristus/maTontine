package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DecaisementBanqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecaisementBanqueDTO.class);
        DecaisementBanqueDTO decaisementBanqueDTO1 = new DecaisementBanqueDTO();
        decaisementBanqueDTO1.setId(1L);
        DecaisementBanqueDTO decaisementBanqueDTO2 = new DecaisementBanqueDTO();
        assertThat(decaisementBanqueDTO1).isNotEqualTo(decaisementBanqueDTO2);
        decaisementBanqueDTO2.setId(decaisementBanqueDTO1.getId());
        assertThat(decaisementBanqueDTO1).isEqualTo(decaisementBanqueDTO2);
        decaisementBanqueDTO2.setId(2L);
        assertThat(decaisementBanqueDTO1).isNotEqualTo(decaisementBanqueDTO2);
        decaisementBanqueDTO1.setId(null);
        assertThat(decaisementBanqueDTO1).isNotEqualTo(decaisementBanqueDTO2);
    }
}
