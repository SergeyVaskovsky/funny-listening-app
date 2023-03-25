package ru.funnylistening.app.service;

import java.util.List;
import java.util.Optional;
import ru.funnylistening.app.domain.Link;

/**
 * Service Interface for managing {@link Link}.
 */
public interface LinkService {
    /**
     * Save a link.
     *
     * @param link the entity to save.
     * @return the persisted entity.
     */
    Link save(Link link);

    /**
     * Updates a link.
     *
     * @param link the entity to update.
     * @return the persisted entity.
     */
    Link update(Link link);

    /**
     * Partially updates a link.
     *
     * @param link the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Link> partialUpdate(Link link);

    /**
     * Get all the links.
     *
     * @return the list of entities.
     */
    List<Link> findAll();

    /**
     * Get the "id" link.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Link> findOne(Long id);

    /**
     * Delete the "id" link.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
