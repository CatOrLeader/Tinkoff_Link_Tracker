package edu.java.scrapper.domain.repository.jpa.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Entity
@Table(name = "tg_chat")
@NoArgsConstructor
@Getter
@Setter
public class EntityTgChat {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false,
            columnDefinition = "varchar(32) default 'UNINITIALIZED'::character varying not null'")
    @Generated(event = EventType.INSERT)
    private @NotBlank String dialogState;

    @Column(nullable = false,
            columnDefinition = "varchar(4) default 'en'")
    @Generated(event = EventType.INSERT)
    private @NotBlank String languageTag;

    @ManyToMany
    @JoinTable(
        name = "chat_link_assignment",
        joinColumns = @JoinColumn(name = "chat_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "link_id", nullable = false)
    )
    private Set<EntityLink> links;
}
