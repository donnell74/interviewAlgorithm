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
import org.eclipse.egit.github.core.client.RequestException;

import com.reorconsultants.models.RepositoryStats;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GitHubAnalyzerService {
    GitHubClient githubClient;
    RepositoryService repositoryService;
    CommitService commitService;
    UserService userService;
    User currUser;
    List<String> currUserEmails;
    List<RepositoryStats> repoStatsList;

    public GitHubAnalyzerService(String username, String password) {
        repoStatsList = new ArrayList<>();

        initGithubClient(username, password);
        initServices();
    }

    private void initGithubClient(String username, String password) {
        // auth
        githubClient = new GitHubClient();
        githubClient.setCredentials(username, password);
    }

    private void initServices() {
        repositoryService = new RepositoryService(githubClient);
        commitService = new CommitService(githubClient);
        userService = new UserService(githubClient);
    }

    public void start() {
        try {
            currUser = userService.getUser();
            currUserEmails = userService.getEmails();
            currUserEmails = currUserEmails == null ? new ArrayList<>() : currUserEmails;
            // get repos
            for (Repository eachRepo : repositoryService.getRepositories()) {
                handleRepo(eachRepo);
                //break; // only do first repository to speed up testing
            }

            // calculate and hog the cpus
            calculateAllRepoStats();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void handleRepo(Repository repo) throws IOException {
        Application.masterDao.insert(repo);
        RepositoryStats repoStats = new RepositoryStats(repo, currUser, commitService);
        // get all commits for repo that were by this user
        try {
            for (RepositoryCommit eachRepoCommit : commitService.getCommits(repo)) {
                if (currUserEmails.contains(eachRepoCommit.getCommit().getAuthor().getEmail())) {
                    RepositoryCommit commit = commitService.getCommit(repo, eachRepoCommit.getSha());
                    // put in db and add to repoStats
                    Application.masterDao.insert(commit, repo);
                    repoStats.addCommit(commit);
                }
            }
        } catch (RequestException ex) {
            System.out.println("Empty repo");
        }

        repoStatsList.add(repoStats);
    }

    private void calculateAllRepoStats() {
        for (RepositoryStats eachRepoStats : repoStatsList) {
            eachRepoStats.calculate();
            System.out.println(eachRepoStats.toString());
        }
    }
}