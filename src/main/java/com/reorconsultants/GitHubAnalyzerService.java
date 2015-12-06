package com.reorconsultants;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.util.List;

public class GitHubAnalyzerService {
    GitHubClient githubClient;
    RepositoryService repositoryService;
    CommitService commitService;
    UserService userService;
    User currUser;

    public GitHubAnalyzerService(String username, String password) {
        initGithubClient(username, password);
        initServices();
        try {
            currUser = userService.getUser();
            List<String> emails = userService.getEmails();
            // get repos
            for (Repository eachRepo : repositoryService.getRepositories()) {
                Application.masterDao.insert(eachRepo);
                for (RepositoryCommit eachRepoCommit : commitService.getCommits(eachRepo)) {
                    if (emails.contains(eachRepoCommit.getCommit().getAuthor().getEmail())) {
                        RepositoryCommit commit = commitService.getCommit(eachRepo, eachRepoCommit.getSha());
                        Application.masterDao.insert(commit, eachRepo);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initGithubClient(String username, String password) {
        // auth
        githubClient = new GitHubClient();
        githubClient.setCredentials(username, password);
    }

    public void initServices() {
        repositoryService = new RepositoryService(githubClient);
        commitService = new CommitService(githubClient);
        userService = new UserService(githubClient);
    }
}