package ru.funnylistening.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.funnylistening.app.domain.ReferalLink;

/**
 * Spring Data JPA repository for the ReferalLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferalLinkRepository extends JpaRepository<ReferalLink, Long> {}
