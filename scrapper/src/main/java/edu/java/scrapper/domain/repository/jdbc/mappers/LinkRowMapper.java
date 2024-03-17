package edu.java.scrapper.domain.repository.jdbc.mappers;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.ResponseType;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Link(
            rs.getLong("id"),
            URI.create(rs.getString("uri")),
            rs.getString("description"),
            OffsetDateTime.from(rs.getTimestamp("created_at").toInstant()),
            OffsetDateTime.from(rs.getTimestamp("updated_at").toInstant()),
            rs.getString("created_by"),
            rs.getString("updated_by"),
            rs.getString("update_description"),
            rs.getString("etag"),
            OffsetDateTime.from(rs.getTimestamp("last_checked_at").toInstant()),
            ResponseType.valueOf(rs.getString("type"))
        );
    }
}
