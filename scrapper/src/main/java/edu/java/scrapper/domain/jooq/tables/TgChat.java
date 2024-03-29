/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.jooq.tables;

import edu.java.scrapper.domain.jooq.DefaultSchema;
import edu.java.scrapper.domain.jooq.Keys;
import edu.java.scrapper.domain.jooq.tables.records.TgChatRecord;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class TgChat extends TableImpl<TgChatRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>TG_CHAT</code>
     */
    public static final TgChat TG_CHAT = new TgChat();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<TgChatRecord> getRecordType() {
        return TgChatRecord.class;
    }

    /**
     * The column <code>TG_CHAT.ID</code>.
     */
    public final TableField<TgChatRecord, String> ID =
        createField(DSL.name("ID"), SQLDataType.VARCHAR(20).nullable(false), this, "");

    /**
     * The column <code>TG_CHAT.DIALOG_STATE</code>.
     */
    public final TableField<TgChatRecord, String> DIALOG_STATE = createField(DSL.name("DIALOG_STATE"),
        SQLDataType.VARCHAR(32).nullable(false)
            .defaultValue(DSL.field(DSL.raw("'UNINITIALIZED'"), SQLDataType.VARCHAR)),
        this,
        ""
    );

    /**
     * The column <code>TG_CHAT.LANGUAGE_TAG</code>.
     */
    public final TableField<TgChatRecord, String> LANGUAGE_TAG =
        createField(DSL.name("LANGUAGE_TAG"), SQLDataType.VARCHAR(4), this, "");

    private TgChat(Name alias, Table<TgChatRecord> aliased) {
        this(alias, aliased, null);
    }

    private TgChat(Name alias, Table<TgChatRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>TG_CHAT</code> table reference
     */
    public TgChat(String alias) {
        this(DSL.name(alias), TG_CHAT);
    }

    /**
     * Create an aliased <code>TG_CHAT</code> table reference
     */
    public TgChat(Name alias) {
        this(alias, TG_CHAT);
    }

    /**
     * Create a <code>TG_CHAT</code> table reference
     */
    public TgChat() {
        this(DSL.name("TG_CHAT"), null);
    }

    public <O extends Record> TgChat(Table<O> child, ForeignKey<O, TgChatRecord> key) {
        super(child, key, TG_CHAT);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<TgChatRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_D;
    }

    @Override
    @NotNull
    public TgChat as(String alias) {
        return new TgChat(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public TgChat as(Name alias) {
        return new TgChat(alias, this);
    }

    @Override
    @NotNull
    public TgChat as(Table<?> alias) {
        return new TgChat(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChat rename(String name) {
        return new TgChat(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChat rename(Name name) {
        return new TgChat(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChat rename(Table<?> name) {
        return new TgChat(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(
        Class<U> toType,
        Function3<? super String, ? super String, ? super String, ? extends U> from
    ) {
        return convertFrom(toType, Records.mapping(from));
    }
}
