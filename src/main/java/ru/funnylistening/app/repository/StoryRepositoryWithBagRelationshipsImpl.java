package ru.funnylistening.app.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.funnylistening.app.domain.Story;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class StoryRepositoryWithBagRelationshipsImpl implements StoryRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Story> fetchBagRelationships(Optional<Story> story) {
        return story.map(this::fetchElements);
    }

    @Override
    public Page<Story> fetchBagRelationships(Page<Story> stories) {
        return new PageImpl<>(fetchBagRelationships(stories.getContent()), stories.getPageable(), stories.getTotalElements());
    }

    @Override
    public List<Story> fetchBagRelationships(List<Story> stories) {
        return Optional.of(stories).map(this::fetchElements).orElse(Collections.emptyList());
    }

    Story fetchElements(Story result) {
        return entityManager
            .createQuery("select story from Story story left join fetch story.elements where story is :story", Story.class)
            .setParameter("story", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Story> fetchElements(List<Story> stories) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, stories.size()).forEach(index -> order.put(stories.get(index).getId(), index));
        List<Story> result = entityManager
            .createQuery("select distinct story from Story story left join fetch story.elements where story in :stories", Story.class)
            .setParameter("stories", stories)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
