package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.repository.jpa.dto.EntityLink;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface JpaLinkRepositoryInterface extends JpaRepository<EntityLink, Long> {
    @Transactional
    @NotNull
    Collection<EntityLink> findAllByLastCheckedAtLessThanEqualOrLastCheckedAtIsNull(@NotBlank OffsetDateTime dateTime);

    @Query(value = "from EntityLink link where link.uri = ?1")
    @Transactional
    @NotNull Optional<EntityLink> findByUri(URI link);
}
