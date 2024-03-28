package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jooq.Tables;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.utils.DateTimeUtils;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dsl;

    @Override
    @Transactional
    public Collection<Link> findAll() {
        return dsl.selectFrom(Tables.LINK)
            .fetchInto(edu.java.scrapper.domain.jooq.tables.pojos.Link.class)
            .stream().map(Link::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Collection<Link> findAllByTgChatId(String tgChatId) {
        return dsl.select(Tables.LINK.fields())
            .from(Tables.LINK)
            .innerJoin(Tables.CHAT_LINK_ASSIGNMENT).on(Tables.LINK.ID.eq(Tables.CHAT_LINK_ASSIGNMENT.LINK_ID))
            .where(Tables.CHAT_LINK_ASSIGNMENT.CHAT_ID.eq(tgChatId))
            .fetchInto(edu.java.scrapper.domain.jooq.tables.pojos.Link.class).stream()
            .map(Link::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Collection<Link> findAllBefore(Timestamp dateTime) {
        return dsl.selectFrom(Tables.LINK)
            .where(Tables.LINK.LAST_CHECKED_AT.le(dateTime.toLocalDateTime()))
            .or(Tables.LINK.LAST_CHECKED_AT.isNull())
            .fetchInto(edu.java.scrapper.domain.jooq.tables.pojos.Link.class).stream()
            .map(Link::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Link> findById(long linkId) {
        return dsl.selectFrom(Tables.LINK)
            .where(Tables.LINK.ID.eq(linkId))
            .fetchOptionalInto(edu.java.scrapper.domain.jooq.tables.pojos.Link.class)
            .map(Link::new);
    }

    @Override
    @Transactional
    public Optional<Link> find(String link) {
        return dsl.selectFrom(Tables.LINK)
            .where(Tables.LINK.URI.eq(link))
            .fetchOptionalInto(edu.java.scrapper.domain.jooq.tables.pojos.Link.class)
            .map(Link::new);
    }

    @Override
    @Transactional
    public Optional<Link> add(String tgChatId, Link link) {
        var maybeLink = find(link.getUri().toString());
        Link fetchedLink;

        if (maybeLink.isPresent()) {
            fetchedLink = maybeLink.get();

            if (dsl.selectCount()
                    .from(Tables.CHAT_LINK_ASSIGNMENT)
                    .where(Tables.CHAT_LINK_ASSIGNMENT.CHAT_ID.eq(tgChatId))
                    .and(Tables.CHAT_LINK_ASSIGNMENT.LINK_ID.eq(fetchedLink.getId()))
                    .fetchSingleInto(Integer.class) > 0
            ) {
                return Optional.empty();
            } else {
                addInAssignmentTable(tgChatId, fetchedLink.getId());
                return Optional.of(fetchedLink);
            }
        }

        return dsl.insertInto(Tables.LINK)
            .columns(Arrays.stream(Tables.LINK.fields()).toList().subList(1, Tables.LINK.fields().length))
            .values(link.getUri().toString(), link.getDescription(),
                DateTimeUtils.parseFrom(link.getCreatedAt()), DateTimeUtils.parseFrom(link.getUpdatedAt()),
                link.getCreatedBy(), link.getUpdatedBy(), link.getTitle(), link.getEtag(),
                DateTimeUtils.parseFrom(link.getLastCheckedAt()), link.getType() != null ? link.getType().name() : null
            )
            .returning()
            .fetchOptionalInto(edu.java.scrapper.domain.jooq.tables.pojos.Link.class)
            .map(Link::new)
            .filter(link1 -> {
                addInAssignmentTable(tgChatId, link1.getId());
                return true;
            });
    }

    @Override
    @Transactional
    public Optional<Link> remove(String tgChatId, long linkId) {
        boolean isDeleted = dsl.deleteFrom(Tables.CHAT_LINK_ASSIGNMENT)
                                .where(Tables.CHAT_LINK_ASSIGNMENT.LINK_ID.eq(linkId))
                                .and(Tables.CHAT_LINK_ASSIGNMENT.CHAT_ID.eq(tgChatId))
                                .execute() > 0;

        if (isDeleted) {
            var link = findById(linkId);
            removePossibleZombie(linkId);
            return link;
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean update(Link link) {
        var maybeLink = find(link.getUri().toString());

        var createdAt = DateTimeUtils.parseFrom(link.getCreatedAt());
        var updatedAt = DateTimeUtils.parseFrom(link.getUpdatedAt());
        var lastCheckedAt = DateTimeUtils.parseFrom(link.getLastCheckedAt());

        return maybeLink.filter(value -> dsl.update(Tables.LINK)
                                             .set(Tables.LINK.URI, link.getUri().toString())
                                             .set(Tables.LINK.DESCRIPTION, link.getDescription())
                                             .set(
                                                 Tables.LINK.CREATED_AT,
                                                 createdAt != null ? createdAt.toLocalDateTime() : null
                                             )
                                             .set(
                                                 Tables.LINK.UPDATED_AT,
                                                 updatedAt != null ? updatedAt.toLocalDateTime() : null
                                             )
                                             .set(Tables.LINK.CREATED_BY, link.getCreatedBy())
                                             .set(Tables.LINK.UPDATED_BY, link.getUpdatedBy())
                                             .set(Tables.LINK.TITLE, link.getTitle())
                                             .set(
                                                 Tables.LINK.LAST_CHECKED_AT,
                                                 lastCheckedAt != null ? lastCheckedAt.toLocalDateTime() : null
                                             )
                                             .set(
                                                 Tables.LINK.TYPE,
                                                 link.getType() != null ? link.getType().name() : null
                                             )
                                             .where(Tables.LINK.ID.eq(value.getId()))
                                             .execute() > 0).isPresent();
    }

    @Override
    @Transactional
    public boolean updateEtag(String linkUri, String etag) {
        return dsl.update(Tables.LINK)
                   .set(Tables.LINK.ETAG, etag)
                   .where(Tables.LINK.URI.eq(linkUri))
                   .execute() > 0;
    }

    private void removePossibleZombie(long linkId) {
        int countOfBonds =
            dsl.selectCount()
                .from(Tables.CHAT_LINK_ASSIGNMENT)
                .where(Tables.CHAT_LINK_ASSIGNMENT.LINK_ID.eq(linkId))
                .fetchSingleInto(Integer.class);

        if (countOfBonds == 0) {
            dsl.deleteFrom(Tables.LINK).where(Tables.LINK.ID.eq(linkId)).execute();
        }
    }

    private void addInAssignmentTable(String tgChatId, long linkId) {
        dsl.insertInto(Tables.CHAT_LINK_ASSIGNMENT)
            .values(tgChatId, linkId).execute();
    }
}
