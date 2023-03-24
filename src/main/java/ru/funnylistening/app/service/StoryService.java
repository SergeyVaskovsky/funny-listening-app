package ru.funnylistening.app.service;

import java.util.List;
import java.util.Optional;
import ru.funnylistening.app.domain.Story;

/**
 * Service Interface for managing {@link Story}.
 */
public interface StoryService {
    /**
     * Save a story.
     *
     * @param story the entity to save.
     * @return the persisted entity.
     */
    Story save(Story story);

    /**
     * Updates a story.
     *
     * @param story the entity to update.
     * @return the persisted entity.
     */
    Story update(Story story);

    /**
     * Partially updates a story.
     *
     * @param story the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Story> partialUpdate(Story story);

    /**
     * Get all the stories.
     *
     * @return the list of entities.
     */
    List<Story> findAll();

    /**
     * Get the "id" story.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Story> findOne(Long id);

    /**
     * Delete the "id" story.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
