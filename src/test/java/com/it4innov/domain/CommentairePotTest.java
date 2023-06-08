package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentairePotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentairePot.class);
        CommentairePot commentairePot1 = new CommentairePot();
        commentairePot1.setId(1L);
        CommentairePot commentairePot2 = new CommentairePot();
        commentairePot2.setId(commentairePot1.getId());
        assertThat(commentairePot1).isEqualTo(commentairePot2);
        commentairePot2.setId(2L);
        assertThat(commentairePot1).isNotEqualTo(commentairePot2);
        commentairePot1.setId(null);
        assertThat(commentairePot1).isNotEqualTo(commentairePot2);
    }
}
