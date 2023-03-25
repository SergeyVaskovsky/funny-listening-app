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
import ru.funnylistening.app.domain.ReferalLink;
import ru.funnylistening.app.repository.ReferalLinkRepository;

/**
 * Integration tests for the {@link ReferalLinkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferalLinkResourceIT {

    private static final String DEFAULT_LINK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_LINK_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/referal-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReferalLinkRepository referalLinkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferalLinkMockMvc;

    private ReferalLink referalLink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferalLink createEntity(EntityManager em) {
        ReferalLink referalLink = new ReferalLink().linkText(DEFAULT_LINK_TEXT);
        return referalLink;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferalLink createUpdatedEntity(EntityManager em) {
        ReferalLink referalLink = new ReferalLink().linkText(UPDATED_LINK_TEXT);
        return referalLink;
    }

    @BeforeEach
    public void initTest() {
        referalLink = createEntity(em);
    }

    @Test
    @Transactional
    void createReferalLink() throws Exception {
        int databaseSizeBeforeCreate = referalLinkRepository.findAll().size();
        // Create the ReferalLink
        restReferalLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referalLink)))
            .andExpect(status().isCreated());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeCreate + 1);
        ReferalLink testReferalLink = referalLinkList.get(referalLinkList.size() - 1);
        assertThat(testReferalLink.getLinkText()).isEqualTo(DEFAULT_LINK_TEXT);
    }

    @Test
    @Transactional
    void createReferalLinkWithExistingId() throws Exception {
        // Create the ReferalLink with an existing ID
        referalLink.setId(1L);

        int databaseSizeBeforeCreate = referalLinkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferalLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referalLink)))
            .andExpect(status().isBadRequest());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLinkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = referalLinkRepository.findAll().size();
        // set the field null
        referalLink.setLinkText(null);

        // Create the ReferalLink, which fails.

        restReferalLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referalLink)))
            .andExpect(status().isBadRequest());

        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReferalLinks() throws Exception {
        // Initialize the database
        referalLinkRepository.saveAndFlush(referalLink);

        // Get all the referalLinkList
        restReferalLinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referalLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].linkText").value(hasItem(DEFAULT_LINK_TEXT)));
    }

    @Test
    @Transactional
    void getReferalLink() throws Exception {
        // Initialize the database
        referalLinkRepository.saveAndFlush(referalLink);

        // Get the referalLink
        restReferalLinkMockMvc
            .perform(get(ENTITY_API_URL_ID, referalLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referalLink.getId().intValue()))
            .andExpect(jsonPath("$.linkText").value(DEFAULT_LINK_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingReferalLink() throws Exception {
        // Get the referalLink
        restReferalLinkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReferalLink() throws Exception {
        // Initialize the database
        referalLinkRepository.saveAndFlush(referalLink);

        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();

        // Update the referalLink
        ReferalLink updatedReferalLink = referalLinkRepository.findById(referalLink.getId()).get();
        // Disconnect from session so that the updates on updatedReferalLink are not directly saved in db
        em.detach(updatedReferalLink);
        updatedReferalLink.linkText(UPDATED_LINK_TEXT);

        restReferalLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReferalLink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReferalLink))
            )
            .andExpect(status().isOk());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
        ReferalLink testReferalLink = referalLinkList.get(referalLinkList.size() - 1);
        assertThat(testReferalLink.getLinkText()).isEqualTo(UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingReferalLink() throws Exception {
        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();
        referalLink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferalLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referalLink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referalLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferalLink() throws Exception {
        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();
        referalLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referalLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferalLink() throws Exception {
        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();
        referalLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalLinkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referalLink)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferalLinkWithPatch() throws Exception {
        // Initialize the database
        referalLinkRepository.saveAndFlush(referalLink);

        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();

        // Update the referalLink using partial update
        ReferalLink partialUpdatedReferalLink = new ReferalLink();
        partialUpdatedReferalLink.setId(referalLink.getId());

        partialUpdatedReferalLink.linkText(UPDATED_LINK_TEXT);

        restReferalLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferalLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferalLink))
            )
            .andExpect(status().isOk());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
        ReferalLink testReferalLink = referalLinkList.get(referalLinkList.size() - 1);
        assertThat(testReferalLink.getLinkText()).isEqualTo(UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateReferalLinkWithPatch() throws Exception {
        // Initialize the database
        referalLinkRepository.saveAndFlush(referalLink);

        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();

        // Update the referalLink using partial update
        ReferalLink partialUpdatedReferalLink = new ReferalLink();
        partialUpdatedReferalLink.setId(referalLink.getId());

        partialUpdatedReferalLink.linkText(UPDATED_LINK_TEXT);

        restReferalLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferalLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferalLink))
            )
            .andExpect(status().isOk());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
        ReferalLink testReferalLink = referalLinkList.get(referalLinkList.size() - 1);
        assertThat(testReferalLink.getLinkText()).isEqualTo(UPDATED_LINK_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingReferalLink() throws Exception {
        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();
        referalLink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferalLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referalLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referalLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferalLink() throws Exception {
        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();
        referalLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referalLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferalLink() throws Exception {
        int databaseSizeBeforeUpdate = referalLinkRepository.findAll().size();
        referalLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalLinkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(referalLink))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferalLink in the database
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferalLink() throws Exception {
        // Initialize the database
        referalLinkRepository.saveAndFlush(referalLink);

        int databaseSizeBeforeDelete = referalLinkRepository.findAll().size();

        // Delete the referalLink
        restReferalLinkMockMvc
            .perform(delete(ENTITY_API_URL_ID, referalLink.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReferalLink> referalLinkList = referalLinkRepository.findAll();
        assertThat(referalLinkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
