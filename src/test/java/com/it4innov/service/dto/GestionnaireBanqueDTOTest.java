package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GestionnaireBanqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GestionnaireBanqueDTO.class);
        GestionnaireBanqueDTO gestionnaireBanqueDTO1 = new GestionnaireBanqueDTO();
        gestionnaireBanqueDTO1.setId(1L);
        GestionnaireBanqueDTO gestionnaireBanqueDTO2 = new GestionnaireBanqueDTO();
        assertThat(gestionnaireBanqueDTO1).isNotEqualTo(gestionnaireBanqueDTO2);
        gestionnaireBanqueDTO2.setId(gestionnaireBanqueDTO1.getId());
        assertThat(gestionnaireBanqueDTO1).isEqualTo(gestionnaireBanqueDTO2);
        gestionnaireBanqueDTO2.setId(2L);
        assertThat(gestionnaireBanqueDTO1).isNotEqualTo(gestionnaireBanqueDTO2);
        gestionnaireBanqueDTO1.setId(null);
        assertThat(gestionnaireBanqueDTO1).isNotEqualTo(gestionnaireBanqueDTO2);
    }
}
