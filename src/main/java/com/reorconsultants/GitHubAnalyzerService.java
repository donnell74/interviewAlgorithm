package com.reorconsultants;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.CommitService;

import java.io.IOException;

public class GitHubAnalyzerService {
    GitHubClient githubClient;
    RepositoryService repositoryService;
    CommitService commitService;

    public GitHubAnalyzerService(String username, String password) {
        initGithubClient(username, password);
        initServices();
        try {
            // get repos
            for (Repository eachRepo : repositoryService.getRepositories()) {
                System.out.println(eachRepo.getName());
                for (RepositoryCommit eachRepoCommit : commitService.getCommits(eachRepo)) {
                    Commit eachCommit = eachRepoCommit.getCommit();
                    System.out.println(eachCommit.getAuthor().getEmail() + " - " + eachCommit.getMessage());
                }
                break;
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
    }
}