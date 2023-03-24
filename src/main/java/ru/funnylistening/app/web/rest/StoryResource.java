package ru.funnylistening.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.funnylistening.app.domain.Story;
import ru.funnylistening.app.repository.StoryRepository;
import ru.funnylistening.app.service.StoryService;
import ru.funnylistening.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.funnylistening.app.domain.Story}.
 */
@RestController
@RequestMapping("/api")
public class StoryResource {

    private final Logger log = LoggerFactory.getLogger(StoryResource.class);

    private static final String ENTITY_NAME = "story";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoryService storyService;

    private final StoryRepository storyRepository;

    public StoryResource(StoryService storyService, StoryRepository storyRepository) {
        this.storyService = storyService;
        this.storyRepository = storyRepository;
    }

    /**
     * {@code POST  /stories} : Create a new story.
     *
     * @param story the story to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new story, or with status {@code 400 (Bad Request)} if the story has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stories")
    public ResponseEntity<Story> createStory(@Valid @RequestBody Story story) throws URISyntaxException {
        log.debug("REST request to save Story : {}", story);
        if (story.getId() != null) {
            throw new BadRequestAlertException("A new story cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Story result = storyService.save(story);
        return ResponseEntity
            .created(new URI("/api/stories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stories/:id} : Updates an existing story.
     *
     * @param id the id of the story to save.
     * @param story the story to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated story,
     * or with status {@code 400 (Bad Request)} if the story is not valid,
     * or with status {@code 500 (Internal Server Error)} if the story couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stories/{id}")
    public ResponseEntity<Story> updateStory(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Story story)
        throws URISyntaxException {
        log.debug("REST request to update Story : {}, {}", id, story);
        if (story.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, story.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Story result = storyService.update(story);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, story.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stories/:id} : Partial updates given fields of an existing story, field will ignore if it is null
     *
     * @param id the id of the story to save.
     * @param story the story to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated story,
     * or with status {@code 400 (Bad Request)} if the story is not valid,
     * or with status {@code 404 (Not Found)} if the story is not found,
     * or with status {@code 500 (Internal Server Error)} if the story couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Story> partialUpdateStory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Story story
    ) throws URISyntaxException {
        log.debug("REST request to partial update Story partially : {}, {}", id, story);
        if (story.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, story.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Story> result = storyService.partialUpdate(story);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, story.getId().toString())
        );
    }

    /**
     * {@code GET  /stories} : get all the stories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stories in body.
     */
    @GetMapping("/stories")
    public List<Story> getAllStories(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Stories");
        return storyService.findAll();
    }

    /**
     * {@code GET  /stories/:id} : get the "id" story.
     *
     * @param id the id of the story to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the story, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stories/{id}")
    public ResponseEntity<Story> getStory(@PathVariable Long id) {
        log.debug("REST request to get Story : {}", id);
        Optional<Story> story = storyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(story);
    }

    /**
     * {@code DELETE  /stories/:id} : delete the "id" story.
     *
     * @param id the id of the story to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stories/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        log.debug("REST request to delete Story : {}", id);
        storyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
