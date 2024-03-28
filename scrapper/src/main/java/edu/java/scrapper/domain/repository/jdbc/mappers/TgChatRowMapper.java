package edu.java.scrapper.domain.repository.jdbc.mappers;

import edu.java.scrapper.domain.dto.TgChat;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TgChatRowMapper implements RowMapper<TgChat> {
    @Override
    public TgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TgChat(
            rs.getLong("id"),
            rs.getString("dialog_state"),
            rs.getString("language_tag")
        );
    }
}
