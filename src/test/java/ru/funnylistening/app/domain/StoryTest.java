package ru.funnylistening.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.funnylistening.app.web.rest.TestUtil;

class StoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Story.class);
        Story story1 = new Story();
        story1.setId(1L);
        Story story2 = new Story();
        story2.setId(story1.getId());
        assertThat(story1).isEqualTo(story2);
        story2.setId(2L);
        assertThat(story1).isNotEqualTo(story2);
        story1.setId(null);
        assertThat(story1).isNotEqualTo(story2);
    }
}
