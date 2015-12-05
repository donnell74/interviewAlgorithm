package com.reorconsultants.db;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.ResultSet;

import org.eclipse.egit.github.core.Repository;

public class RepoDAO {
    private Session session;

    public RepoDAO(Session session) {
        this.session = session;
    }

    public long insert(Repository row) {
        session.execute(String.format("INSERT INTO repos (repo_id, name, url)" +
            " VALUES (%d, '%s', '%s') IF NOT EXISTS", row.getId(), row.getName(), row.getUrl()));

        return row.getId();
    }
}