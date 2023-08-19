package de.munichdeveloper.user.repository;

import de.munichdeveloper.user.domain.MagicLink;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface MagicLinkRepository extends JpaRepository<MagicLink, Integer> {
}
