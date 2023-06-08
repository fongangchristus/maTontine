package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriquePersonneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriquePersonne.class);
        HistoriquePersonne historiquePersonne1 = new HistoriquePersonne();
        historiquePersonne1.setId(1L);
        HistoriquePersonne historiquePersonne2 = new HistoriquePersonne();
        historiquePersonne2.setId(historiquePersonne1.getId());
        assertThat(historiquePersonne1).isEqualTo(historiquePersonne2);
        historiquePersonne2.setId(2L);
        assertThat(historiquePersonne1).isNotEqualTo(historiquePersonne2);
        historiquePersonne1.setId(null);
        assertThat(historiquePersonne1).isNotEqualTo(historiquePersonne2);
    }
}
