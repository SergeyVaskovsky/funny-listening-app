package ru.funnylistening.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.funnylistening.app.domain.Element;

/**
 * Spring Data JPA repository for the Element entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElementRepository extends JpaRepository<Element, Long> {}
