package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionTontineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionTontine.class);
        SessionTontine sessionTontine1 = new SessionTontine();
        sessionTontine1.setId(1L);
        SessionTontine sessionTontine2 = new SessionTontine();
        sessionTontine2.setId(sessionTontine1.getId());
        assertThat(sessionTontine1).isEqualTo(sessionTontine2);
        sessionTontine2.setId(2L);
        assertThat(sessionTontine1).isNotEqualTo(sessionTontine2);
        sessionTontine1.setId(null);
        assertThat(sessionTontine1).isNotEqualTo(sessionTontine2);
    }
}
