package ru.funnylistening.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.funnylistening.app.domain.Story;

public interface StoryRepositoryWithBagRelationships {
    Optional<Story> fetchBagRelationships(Optional<Story> story);

    List<Story> fetchBagRelationships(List<Story> stories);

    Page<Story> fetchBagRelationships(Page<Story> stories);
}
