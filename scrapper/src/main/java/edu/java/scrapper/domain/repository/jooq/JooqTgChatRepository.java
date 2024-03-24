package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.dto.TgChat;
import edu.java.scrapper.domain.jooq.Tables;
import edu.java.scrapper.domain.repository.TgChatRepository;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.defaultValue;

@RequiredArgsConstructor
public class JooqTgChatRepository implements TgChatRepository {
    private final DSLContext dsl;

    @Override
    public boolean register(String tgChatId) {
        if (find(tgChatId).isPresent()) {
            return false;
        }

        return dsl.insertInto(Tables.TG_CHAT)
                   .values(
                       tgChatId,
                       defaultValue(Tables.TG_CHAT.DIALOG_STATE),
                       Locale.ENGLISH.toLanguageTag()
                   ).execute() > 0;
    }

    @Override
    public Optional<TgChat> find(String tgChatId) {
        return dsl.selectFrom(Tables.TG_CHAT)
            .where(Tables.TG_CHAT.ID.eq(tgChatId))
            .fetchOptionalInto(edu.java.scrapper.domain.jooq.tables.pojos.TgChat.class)
            .map(TgChat::new);
    }

    @Override
    public Collection<TgChat> findAll() {
        return dsl.selectFrom(Tables.TG_CHAT)
            .fetchInto(edu.java.scrapper.domain.jooq.tables.pojos.TgChat.class).stream()
            .map(TgChat::new).collect(Collectors.toList());
    }

    @Override
    public Collection<TgChat> findAllByLinkUrl(String linkUrl) {
        return dsl.selectFrom(
                Tables.TG_CHAT
                    .innerJoin(Tables.CHAT_LINK_ASSIGNMENT)
                    .on(Tables.TG_CHAT.ID.eq(Tables.CHAT_LINK_ASSIGNMENT.CHAT_ID))
                    .innerJoin(Tables.LINK)
                    .on(Tables.CHAT_LINK_ASSIGNMENT.LINK_ID.eq(Tables.LINK.ID))
            )
            .where(Tables.LINK.URI.eq(linkUrl))
            .fetchInto(edu.java.scrapper.domain.jooq.tables.pojos.TgChat.class).stream()
            .map(TgChat::new).collect(Collectors.toList());
    }

    @Override
    public boolean update(String tgChatId, String dialogState, String languageTag) {
        if (find(tgChatId).isEmpty()) {
            return false;
        }

        return dsl.update(Tables.TG_CHAT)
                   .set(Tables.TG_CHAT.DIALOG_STATE, dialogState)
                   .set(Tables.TG_CHAT.LANGUAGE_TAG, languageTag)
                   .where(Tables.TG_CHAT.ID.eq(tgChatId))
                   .execute() > 0;
    }

    @Override
    public boolean remove(String tgChatId) {
        if (find(tgChatId).isEmpty()) {
            return false;
        }

        dsl.deleteFrom(Tables.CHAT_LINK_ASSIGNMENT)
            .where(Tables.CHAT_LINK_ASSIGNMENT.CHAT_ID.eq(tgChatId))
            .execute();

        return dsl.deleteFrom(Tables.TG_CHAT)
                   .where(Tables.TG_CHAT.ID.eq(tgChatId))
                   .execute() > 0;
    }
}
