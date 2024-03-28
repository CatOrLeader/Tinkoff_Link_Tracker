package edu.java.scrapper.domain.repository.jdbc.mappers;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.ResponseType;
import edu.java.scrapper.utils.DateTimeUtils;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        var createdAt = rs.getTimestamp("created_at");
        var updatedAt = rs.getTimestamp("updated_at");
        var lastCheckedAt = rs.getTimestamp("last_checked_at");
        var type = rs.getString("type");

        return new Link(
            rs.getLong("id"),
            URI.create(rs.getString("uri")),
            rs.getString("description"),
            createdAt == null ? null : DateTimeUtils.parseFrom(createdAt),
            updatedAt == null ? null : DateTimeUtils.parseFrom(updatedAt),
            rs.getString("created_by"),
            rs.getString("updated_by"),
            rs.getString("title"),
            rs.getString("etag"),
            lastCheckedAt == null ? null : DateTimeUtils.parseFrom(lastCheckedAt),
            type == null ? null : ResponseType.valueOf(type)
        );
    }
}
