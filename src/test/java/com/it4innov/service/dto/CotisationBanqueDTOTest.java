package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CotisationBanqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CotisationBanqueDTO.class);
        CotisationBanqueDTO cotisationBanqueDTO1 = new CotisationBanqueDTO();
        cotisationBanqueDTO1.setId(1L);
        CotisationBanqueDTO cotisationBanqueDTO2 = new CotisationBanqueDTO();
        assertThat(cotisationBanqueDTO1).isNotEqualTo(cotisationBanqueDTO2);
        cotisationBanqueDTO2.setId(cotisationBanqueDTO1.getId());
        assertThat(cotisationBanqueDTO1).isEqualTo(cotisationBanqueDTO2);
        cotisationBanqueDTO2.setId(2L);
        assertThat(cotisationBanqueDTO1).isNotEqualTo(cotisationBanqueDTO2);
        cotisationBanqueDTO1.setId(null);
        assertThat(cotisationBanqueDTO1).isNotEqualTo(cotisationBanqueDTO2);
    }
}
