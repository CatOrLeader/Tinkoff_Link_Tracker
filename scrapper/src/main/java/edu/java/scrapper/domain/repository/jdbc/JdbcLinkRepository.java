package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.jdbc.mappers.LinkRowMapper;
import edu.java.scrapper.utils.DateTimeUtils;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final TransactionTemplate transactionTemplate;
    private final JdbcClient jdbcClient;
    private final LinkRowMapper linkRowMapper;

    @Override
    public Optional<Link> add(String tgChatId, Link link) {
        return transactionTemplate.execute(status -> {
            var maybeLink = find(link.getUri().toString());
            Link fetchedLink;
            if (maybeLink.isPresent()) {
                fetchedLink = maybeLink.get();

                if (jdbcClient.sql("SELECT COUNT(*) FROM chat_link_assignment WHERE chat_id = ? AND link_id = ?")
                        .param(tgChatId)
                        .param(fetchedLink.getId())
                        .query(Integer.class).single() > 0
                ) {
                    return Optional.empty();
                } else {
                    addInAssignmentTable(tgChatId, fetchedLink.getId());
                    return Optional.of(fetchedLink);
                }
            }

            fetchedLink = jdbcClient.sql(
                    "INSERT INTO link (uri, description, created_at, updated_at, created_by, "
                    + "updated_by, title, etag, last_checked_at, type) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                    + "RETURNING *")
                .param(link.getUri().toString())
                .param(link.getDescription())
                .param(DateTimeUtils.parseFrom(link.getCreatedAt()))
                .param(DateTimeUtils.parseFrom(link.getUpdatedAt()))
                .param(link.getCreatedBy())
                .param(link.getUpdatedBy())
                .param(link.getTitle())
                .param(link.getEtag())
                .param(DateTimeUtils.parseFrom(link.getLastCheckedAt()))
                .param(link.getType() == null ? null : link.getType().name())
                .query(linkRowMapper).single();
            addInAssignmentTable(tgChatId, fetchedLink.getId());

            return Optional.of(fetchedLink);
        });
    }

    @Override
    public Optional<Link> remove(String tgChatId, @NotBlank long linkId) {
        return transactionTemplate.execute(status -> {
            if (jdbcClient.sql("DELETE FROM chat_link_assignment WHERE chat_id = ? AND link_id = ?")
                    .params(tgChatId, linkId).update() == 0) {
                return Optional.empty();
            }

            var link = findById(linkId);
            removePossibleZombie(linkId);
            return link;
        });
    }

    @Override
    public Collection<Link> findAll() {
        return transactionTemplate.execute(status -> jdbcClient.sql("SELECT * FROM link")
            .query(linkRowMapper).list());
    }

    @Override
    public Collection<Link> findAllByTgChatId(String tgChatId) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM link, chat_link_assignment "
                           + "WHERE link.id = chat_link_assignment.link_id AND chat_id = ?")
                .param(tgChatId)
                .query(linkRowMapper)
                .list()
        );
    }

    @Override
    public Collection<Link> findAllBefore(Timestamp dateTime) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM link WHERE link.last_checked_at <= ? OR link.last_checked_at IS NULL")
                .param(dateTime)
                .query(linkRowMapper)
                .list()
        );
    }

    @Override
    public Optional<Link> findById(long linkId) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM link WHERE id = ?")
                .param(linkId)
                .query(linkRowMapper)
                .optional()
        );
    }

    @Override
    public Optional<Link> find(String linkUri) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM link WHERE uri = ?")
                .param(linkUri)
                .query(linkRowMapper)
                .optional()
        );
    }

    @Override
    public boolean update(Link link) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeLink = find(link.getUri().toString());

            return maybeLink.filter(value ->
                jdbcClient.sql("UPDATE link SET uri = :uri, description = :description, created_at = :createdAt, "
                               + "updated_at = :updatedAt, created_by = :createdBy, updated_by = :updatedBy, "
                               + "title = :title, last_checked_at = :lastCheckedAt, type = :type WHERE id = :id")
                    .param("uri", link.getUri().toString())
                    .param("description", link.getDescription())
                    .param("createdAt", DateTimeUtils.parseFrom(link.getCreatedAt()))
                    .param("updatedAt", DateTimeUtils.parseFrom(link.getUpdatedAt()))
                    .param("createdBy", link.getCreatedBy())
                    .param("updatedBy", link.getUpdatedBy())
                    .param("title", link.getTitle())
                    .param("lastCheckedAt", DateTimeUtils.parseFrom(link.getLastCheckedAt()))
                    .param("type", link.getType().name())
                    .param("id", value.getId())
                    .update() > 0).isPresent();

        }));
    }

    @Override
    public boolean updateEtag(String linkUri, String etag) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeLink = find(linkUri);

            return maybeLink.filter(link ->
                jdbcClient.sql("UPDATE link SET etag = ? WHERE uri = ?").params(etag, linkUri).update() > 0
            ).isPresent();
        }));
    }

    private void removePossibleZombie(long linkId) {
        int countOfBonds = jdbcClient.sql("SELECT COUNT(*) FROM chat_link_assignment WHERE link_id = ?")
            .param(linkId).query(Integer.class).single();

        if (countOfBonds == 0) {
            jdbcClient.sql("DELETE FROM link WHERE id = ?").param(linkId).update();
        }
    }

    private void addInAssignmentTable(String tgChatId, long linkId) {
        String insertIntoAssignment = "INSERT INTO chat_link_assignment VALUES (?, ?)";

        jdbcClient.sql(insertIntoAssignment)
            .params(tgChatId, linkId)
            .update();
    }
}
