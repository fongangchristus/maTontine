package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementBanqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementBanqueDTO.class);
        PaiementBanqueDTO paiementBanqueDTO1 = new PaiementBanqueDTO();
        paiementBanqueDTO1.setId(1L);
        PaiementBanqueDTO paiementBanqueDTO2 = new PaiementBanqueDTO();
        assertThat(paiementBanqueDTO1).isNotEqualTo(paiementBanqueDTO2);
        paiementBanqueDTO2.setId(paiementBanqueDTO1.getId());
        assertThat(paiementBanqueDTO1).isEqualTo(paiementBanqueDTO2);
        paiementBanqueDTO2.setId(2L);
        assertThat(paiementBanqueDTO1).isNotEqualTo(paiementBanqueDTO2);
        paiementBanqueDTO1.setId(null);
        assertThat(paiementBanqueDTO1).isNotEqualTo(paiementBanqueDTO2);
    }
}
