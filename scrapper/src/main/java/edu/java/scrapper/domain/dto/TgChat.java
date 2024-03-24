package edu.java.scrapper.domain.dto;

import edu.java.scrapper.domain.repository.jpa.dto.EntityTgChat;
import edu.java.scrapper.rest.model.UpdateChatRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TgChat {
    private long id;

    private @NotBlank String dialogState;
    private @NotBlank String languageTag;

    public TgChat(@NotNull UpdateChatRequest request) {
        this(request.userId(), request.botState(), request.locale());
    }

    public TgChat(@NotNull edu.java.scrapper.domain.jooq.tables.pojos.TgChat tgChat) {
        this(Long.parseLong(tgChat.getId()), tgChat.getDialogState(), tgChat.getLanguageTag());
    }

    public TgChat(@NotNull EntityTgChat tgChat) {
        this(Long.parseLong(tgChat.getId()), tgChat.getDialogState(), tgChat.getLanguageTag());
    }
}
