package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementTontineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementTontineDTO.class);
        PaiementTontineDTO paiementTontineDTO1 = new PaiementTontineDTO();
        paiementTontineDTO1.setId(1L);
        PaiementTontineDTO paiementTontineDTO2 = new PaiementTontineDTO();
        assertThat(paiementTontineDTO1).isNotEqualTo(paiementTontineDTO2);
        paiementTontineDTO2.setId(paiementTontineDTO1.getId());
        assertThat(paiementTontineDTO1).isEqualTo(paiementTontineDTO2);
        paiementTontineDTO2.setId(2L);
        assertThat(paiementTontineDTO1).isNotEqualTo(paiementTontineDTO2);
        paiementTontineDTO1.setId(null);
        assertThat(paiementTontineDTO1).isNotEqualTo(paiementTontineDTO2);
    }
}
