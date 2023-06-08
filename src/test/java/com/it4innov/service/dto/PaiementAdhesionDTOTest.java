package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementAdhesionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementAdhesionDTO.class);
        PaiementAdhesionDTO paiementAdhesionDTO1 = new PaiementAdhesionDTO();
        paiementAdhesionDTO1.setId(1L);
        PaiementAdhesionDTO paiementAdhesionDTO2 = new PaiementAdhesionDTO();
        assertThat(paiementAdhesionDTO1).isNotEqualTo(paiementAdhesionDTO2);
        paiementAdhesionDTO2.setId(paiementAdhesionDTO1.getId());
        assertThat(paiementAdhesionDTO1).isEqualTo(paiementAdhesionDTO2);
        paiementAdhesionDTO2.setId(2L);
        assertThat(paiementAdhesionDTO1).isNotEqualTo(paiementAdhesionDTO2);
        paiementAdhesionDTO1.setId(null);
        assertThat(paiementAdhesionDTO1).isNotEqualTo(paiementAdhesionDTO2);
    }
}
