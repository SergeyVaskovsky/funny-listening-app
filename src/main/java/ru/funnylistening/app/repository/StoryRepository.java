package ru.funnylistening.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.funnylistening.app.domain.Story;

/**
 * Spring Data JPA repository for the Story entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {}
