package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementSanctionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementSanctionDTO.class);
        PaiementSanctionDTO paiementSanctionDTO1 = new PaiementSanctionDTO();
        paiementSanctionDTO1.setId(1L);
        PaiementSanctionDTO paiementSanctionDTO2 = new PaiementSanctionDTO();
        assertThat(paiementSanctionDTO1).isNotEqualTo(paiementSanctionDTO2);
        paiementSanctionDTO2.setId(paiementSanctionDTO1.getId());
        assertThat(paiementSanctionDTO1).isEqualTo(paiementSanctionDTO2);
        paiementSanctionDTO2.setId(2L);
        assertThat(paiementSanctionDTO1).isNotEqualTo(paiementSanctionDTO2);
        paiementSanctionDTO1.setId(null);
        assertThat(paiementSanctionDTO1).isNotEqualTo(paiementSanctionDTO2);
    }
}
