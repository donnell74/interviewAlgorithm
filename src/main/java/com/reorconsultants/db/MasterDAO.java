package com.reorconsultants.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.ResultSet;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.RepositoryCommit;

public class MasterDAO {
    private static Cluster cluster;
    private static Session session;
    private RepoDAO repoDao;
    private CommitDAO commitDao;

    public MasterDAO() {
        // Connect to the cluster and keyspace "interview"
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("interview");

        initChildDaos();
    }

    public void initChildDaos() {
        repoDao = new RepoDAO(session);
        commitDao = new CommitDAO(session);
    }

    public void destroy() {
        // Clean up the connection by closing it
        cluster.close();
    }

    public long insert(Repository repo) {
        return repoDao.insert(repo);
    }

    public String insert(RepositoryCommit commit, Repository repo) {
        return commitDao.insert(commit, repo);
    }
}