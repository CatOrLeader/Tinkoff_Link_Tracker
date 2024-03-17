package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.jdbc.mappers.LinkRowMapper;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final TransactionTemplate transactionTemplate;
    private final JdbcClient jdbcClient;
    private final LinkRowMapper linkRowMapper;

    @Override
    public Link add(long tgChatId, URI link) {
        String insertIntoAssignment = "INSERT INTO chat_link_assignment VALUES (?, ?)";

        return transactionTemplate.execute(status -> {
            var maybeLink = find(link);
            Link fetchedLink;
            if (maybeLink.isPresent()) {
                fetchedLink = maybeLink.get();

                if (jdbcClient.sql("SELECT COUNT(*) FROM chat_link_assignment WHERE chat_id = ? AND link_id = ?")
                        .param(tgChatId)
                        .param(fetchedLink.getId())
                        .query(Integer.class).single() > 0
                ) {
                    throw new RuntimeException(
                        "The link already exist: " + link + " and assigned with the tg chat id " + tgChatId);
                } else {
                    jdbcClient.sql(insertIntoAssignment)
                        .params(tgChatId, fetchedLink.getId())
                        .update();
                    return fetchedLink;
                }
            }

            jdbcClient.sql(
                    "INSERT INTO link (uri, description, created_at, updated_at, created_by, "
                    + "updated_by, update_description, etag, last_checked_at, type) "
                    + "VALUES (?, NULL, NULL, NULL, NULL, NULL, NULL, NULL, DEFAULT, NULL)")
                .param(link.toString())
                .update();
            fetchedLink = find(link).orElseThrow(() -> new RuntimeException("After insert link is not found"));
            jdbcClient.sql(insertIntoAssignment)
                .params(tgChatId, fetchedLink.getId())
                .update();
            return fetchedLink;
        });
    }

    @Override
    public Optional<Link> remove(long tgChatId, long linkId) {
        return transactionTemplate.execute(status -> {
            var maybeLink = find(linkId);
            if (maybeLink.isEmpty()) {
                return Optional.empty();
            }
            var fetchedLink = maybeLink.get();

            jdbcClient.sql("DELETE FROM chat_link_assignment WHERE link_id = ?").params(linkId).update();
            return Optional.of(fetchedLink);
        });
    }

    @Override
    public List<Link> findAll() {
        return transactionTemplate.execute(status -> jdbcClient.sql("SELECT * FROM link")
            .query(linkRowMapper).list());
    }

    @Override
    public List<Link> findAllByTgChatId(long tgChatId) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM link, chat_link_assignment "
                           + "WHERE link.id = chat_link_assignment.link_id AND chat_id = ?")
                .param(tgChatId)
                .query(linkRowMapper)
                .list()
        );
    }

    @Override
    public Optional<Link> find(long linkId) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM link WHERE id = ?")
                .param(linkId)
                .query(linkRowMapper)
                .optional()
        );
    }

    @Override
    public Optional<Link> find(URI link) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM link WHERE uri = ?")
                .param(link.toString())
                .query(linkRowMapper)
                .optional()
        );
    }

    @Override
    public boolean update(Link link) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeLink = find(link.getId());
            if (maybeLink.isEmpty()) {
                return false;
            }

            return jdbcClient.sql("UPDATE link SET uri = :uri, description = :description, created_at = :createdAt, "
                                  + "updated_at = :updatedAt, created_by = :createdBt, updated_by = :updatedBy, "
                                  + "update_description = :updateDescription, etag = :etag, "
                                  + "last_checked_at = :lastCheckedAt, type = :type WHERE id = :id")
                       .paramSource(link)
                       .update() > 0;
        }));
    }
}
