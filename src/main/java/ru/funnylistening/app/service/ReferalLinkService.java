package ru.funnylistening.app.service;

import java.util.List;
import java.util.Optional;
import ru.funnylistening.app.domain.ReferalLink;

/**
 * Service Interface for managing {@link ReferalLink}.
 */
public interface ReferalLinkService {
    /**
     * Save a referalLink.
     *
     * @param referalLink the entity to save.
     * @return the persisted entity.
     */
    ReferalLink save(ReferalLink referalLink);

    /**
     * Updates a referalLink.
     *
     * @param referalLink the entity to update.
     * @return the persisted entity.
     */
    ReferalLink update(ReferalLink referalLink);

    /**
     * Partially updates a referalLink.
     *
     * @param referalLink the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReferalLink> partialUpdate(ReferalLink referalLink);

    /**
     * Get all the referalLinks.
     *
     * @return the list of entities.
     */
    List<ReferalLink> findAll();

    /**
     * Get the "id" referalLink.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReferalLink> findOne(Long id);

    /**
     * Delete the "id" referalLink.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
