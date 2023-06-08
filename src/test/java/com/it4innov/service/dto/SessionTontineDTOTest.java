package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionTontineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionTontineDTO.class);
        SessionTontineDTO sessionTontineDTO1 = new SessionTontineDTO();
        sessionTontineDTO1.setId(1L);
        SessionTontineDTO sessionTontineDTO2 = new SessionTontineDTO();
        assertThat(sessionTontineDTO1).isNotEqualTo(sessionTontineDTO2);
        sessionTontineDTO2.setId(sessionTontineDTO1.getId());
        assertThat(sessionTontineDTO1).isEqualTo(sessionTontineDTO2);
        sessionTontineDTO2.setId(2L);
        assertThat(sessionTontineDTO1).isNotEqualTo(sessionTontineDTO2);
        sessionTontineDTO1.setId(null);
        assertThat(sessionTontineDTO1).isNotEqualTo(sessionTontineDTO2);
    }
}
