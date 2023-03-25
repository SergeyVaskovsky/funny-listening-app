package ru.funnylistening.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.funnylistening.app.web.rest.TestUtil;

class ReferalLinkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferalLink.class);
        ReferalLink referalLink1 = new ReferalLink();
        referalLink1.setId(1L);
        ReferalLink referalLink2 = new ReferalLink();
        referalLink2.setId(referalLink1.getId());
        assertThat(referalLink1).isEqualTo(referalLink2);
        referalLink2.setId(2L);
        assertThat(referalLink1).isNotEqualTo(referalLink2);
        referalLink1.setId(null);
        assertThat(referalLink1).isNotEqualTo(referalLink2);
    }
}
