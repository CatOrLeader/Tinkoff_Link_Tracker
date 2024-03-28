package edu.java.scrapper.domain.repository.jpa.dto;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.ResponseType;
import edu.java.scrapper.domain.repository.jpa.dto.converters.DateTimeConverter;
import edu.java.scrapper.domain.repository.jpa.dto.converters.ResponseTypeConverter;
import edu.java.scrapper.domain.repository.jpa.dto.converters.UrlConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "link")
@NoArgsConstructor
@Getter
@Setter
public class EntityLink {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Convert(converter = UrlConverter.class)
    private @NotNull URI uri;

    private String description;

    @Convert(converter = DateTimeConverter.class)
    private OffsetDateTime createdAt;

    @Convert(converter = DateTimeConverter.class)
    private OffsetDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
    private String title;
    private String etag;

    @Convert(converter = DateTimeConverter.class)
    private OffsetDateTime lastCheckedAt;

    @Convert(converter = ResponseTypeConverter.class)
    private ResponseType type;

    @ManyToMany(mappedBy = "links")
    private Set<EntityTgChat> tgChats;

    public EntityLink(@NotNull Link link, @NotNull EntityTgChat tgChat) {
        this.updateFrom(link);
        this.updateEtag(link.getEtag());
        this.tgChats = Set.of(tgChat);
        tgChat.getLinks().add(this);
    }

    public void updateFrom(@NotNull Link link) {
        this.uri = link.getUri();
        this.description = link.getDescription();
        this.createdAt = link.getCreatedAt();
        this.updatedAt = link.getUpdatedAt();
        this.createdBy = link.getCreatedBy();
        this.updatedBy = link.getUpdatedBy();
        this.title = link.getTitle();
        this.lastCheckedAt = link.getLastCheckedAt();
        this.type = link.getType();
    }

    public void updateEtag(@NotBlank String etag) {
        this.etag = etag;
    }
}
