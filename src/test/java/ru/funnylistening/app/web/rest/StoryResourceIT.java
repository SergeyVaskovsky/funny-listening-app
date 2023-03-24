package ru.funnylistening.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.funnylistening.app.IntegrationTest;
import ru.funnylistening.app.domain.Story;
import ru.funnylistening.app.repository.StoryRepository;

/**
 * Integration tests for the {@link StoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoryResourceIT {

    private static final String DEFAULT_STORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoryMockMvc;

    private Story story;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Story createEntity(EntityManager em) {
        Story story = new Story().storyName(DEFAULT_STORY_NAME);
        return story;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Story createUpdatedEntity(EntityManager em) {
        Story story = new Story().storyName(UPDATED_STORY_NAME);
        return story;
    }

    @BeforeEach
    public void initTest() {
        story = createEntity(em);
    }

    @Test
    @Transactional
    void createStory() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();
        // Create the Story
        restStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isCreated());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate + 1);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getStoryName()).isEqualTo(DEFAULT_STORY_NAME);
    }

    @Test
    @Transactional
    void createStoryWithExistingId() throws Exception {
        // Create the Story with an existing ID
        story.setId(1L);

        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storyRepository.findAll().size();
        // set the field null
        story.setStoryName(null);

        // Create the Story, which fails.

        restStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isBadRequest());

        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStories() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get all the storyList
        restStoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(story.getId().intValue())))
            .andExpect(jsonPath("$.[*].storyName").value(hasItem(DEFAULT_STORY_NAME)));
    }

    @Test
    @Transactional
    void getStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get the story
        restStoryMockMvc
            .perform(get(ENTITY_API_URL_ID, story.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(story.getId().intValue()))
            .andExpect(jsonPath("$.storyName").value(DEFAULT_STORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStory() throws Exception {
        // Get the story
        restStoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story
        Story updatedStory = storyRepository.findById(story.getId()).get();
        // Disconnect from session so that the updates on updatedStory are not directly saved in db
        em.detach(updatedStory);
        updatedStory.storyName(UPDATED_STORY_NAME);

        restStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStory))
            )
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getStoryName()).isEqualTo(UPDATED_STORY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, story.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(story))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(story))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStoryWithPatch() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story using partial update
        Story partialUpdatedStory = new Story();
        partialUpdatedStory.setId(story.getId());

        partialUpdatedStory.storyName(UPDATED_STORY_NAME);

        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStory))
            )
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getStoryName()).isEqualTo(UPDATED_STORY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateStoryWithPatch() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story using partial update
        Story partialUpdatedStory = new Story();
        partialUpdatedStory.setId(story.getId());

        partialUpdatedStory.storyName(UPDATED_STORY_NAME);

        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStory))
            )
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getStoryName()).isEqualTo(UPDATED_STORY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, story.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(story))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(story))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeDelete = storyRepository.findAll().size();

        // Delete the story
        restStoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, story.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
