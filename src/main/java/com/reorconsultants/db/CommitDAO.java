package com.reorconsultants.db;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.ResultSet;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitStats;
import org.eclipse.egit.github.core.RepositoryCommit;

import org.apache.commons.lang3.StringEscapeUtils;

public class CommitDAO {
    private Session session;

    public CommitDAO (Session session) {
        this.session = session;
    }

    public String insert(RepositoryCommit row, Repository repo) {
        CommitStats eachStat = row.getStats();
        session.execute(String.format(
            "INSERT INTO commits (sha, repo_id, message, url, author, deletions, additions, total)" +
            " VALUES ('%s', %d, '%s', '%s', '%s', %d, %d, %d) IF NOT EXISTS", row.getSha(), repo.getId(),
            row.getCommit().getMessage().replaceAll("\'", "\""),
            row.getUrl(), row.getCommit().getAuthor().getEmail(),
            eachStat.getDeletions(), eachStat.getAdditions(), eachStat.getTotal()));

        return row.getSha();
    }
}