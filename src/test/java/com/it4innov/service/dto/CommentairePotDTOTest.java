package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentairePotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentairePotDTO.class);
        CommentairePotDTO commentairePotDTO1 = new CommentairePotDTO();
        commentairePotDTO1.setId(1L);
        CommentairePotDTO commentairePotDTO2 = new CommentairePotDTO();
        assertThat(commentairePotDTO1).isNotEqualTo(commentairePotDTO2);
        commentairePotDTO2.setId(commentairePotDTO1.getId());
        assertThat(commentairePotDTO1).isEqualTo(commentairePotDTO2);
        commentairePotDTO2.setId(2L);
        assertThat(commentairePotDTO1).isNotEqualTo(commentairePotDTO2);
        commentairePotDTO1.setId(null);
        assertThat(commentairePotDTO1).isNotEqualTo(commentairePotDTO2);
    }
}
