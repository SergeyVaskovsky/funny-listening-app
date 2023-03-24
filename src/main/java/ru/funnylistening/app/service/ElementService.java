package ru.funnylistening.app.service;

import java.util.List;
import java.util.Optional;
import ru.funnylistening.app.domain.Element;

/**
 * Service Interface for managing {@link Element}.
 */
public interface ElementService {
    /**
     * Save a element.
     *
     * @param element the entity to save.
     * @return the persisted entity.
     */
    Element save(Element element);

    /**
     * Updates a element.
     *
     * @param element the entity to update.
     * @return the persisted entity.
     */
    Element update(Element element);

    /**
     * Partially updates a element.
     *
     * @param element the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Element> partialUpdate(Element element);

    /**
     * Get all the elements.
     *
     * @return the list of entities.
     */
    List<Element> findAll();
    /**
     * Get all the Element where EntireStory is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Element> findAllWhereEntireStoryIsNull();

    /**
     * Get the "id" element.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Element> findOne(Long id);

    /**
     * Delete the "id" element.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
