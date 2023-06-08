package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormuleAdhesionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormuleAdhesion.class);
        FormuleAdhesion formuleAdhesion1 = new FormuleAdhesion();
        formuleAdhesion1.setId(1L);
        FormuleAdhesion formuleAdhesion2 = new FormuleAdhesion();
        formuleAdhesion2.setId(formuleAdhesion1.getId());
        assertThat(formuleAdhesion1).isEqualTo(formuleAdhesion2);
        formuleAdhesion2.setId(2L);
        assertThat(formuleAdhesion1).isNotEqualTo(formuleAdhesion2);
        formuleAdhesion1.setId(null);
        assertThat(formuleAdhesion1).isNotEqualTo(formuleAdhesion2);
    }
}
