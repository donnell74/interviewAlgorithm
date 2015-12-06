package com.reorconsultants.models;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.CommitStats;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.CommitService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;

import com.reorconsultants.utils.ExtensionsUtil;

public class RepositoryStats {
    User user;
    Repository repo;
    CommitService commitService;

    // overall totals
    int lineAdditions;
    int lineDeletions;
    int commitTotal;

    // language totals
    HashMap<String, CommitStats> languageTotalsMap;

    // list of all commits that have been added
    List<RepositoryCommit> commitsList;

    public RepositoryStats(Repository repo, User user, CommitService commitService) {
        this.repo = repo;
        this.user = user;
        this.commitService = commitService;
        languageTotalsMap = new HashMap<>();
        commitsList = new ArrayList<>();
    }

    /**
    * Add commit to this repoStats, must call calculate for commit to be in calculations
    */
    public void addCommit(RepositoryCommit commit) throws IOException{
        // RepositoryCommits may be half baked when fetched in bulk so refetch by itself
        if (commit.getFiles() == null) {
            System.out.println("Refecthing for commit with sha: " + commit.getSha());
            commit = commitService.getCommit(repo, commit.getSha());
        }

        commitsList.add(commit);
    }

    public void calculate() {
        for (RepositoryCommit eachCommit : commitsList) {
            for (CommitFile eachFile : eachCommit.getFiles()) {
                String language = ExtensionsUtil.getLanguage(eachFile, repo.getLanguage());
                if (language != null) {
                    //System.out.println("Adding file to repoStats: " + eachFile.getFilename());
                    addCommitStats(eachFile, language);
                } else {
                    //System.out.println("Skipping file in calculations: " + eachFile.getFilename());
                }
            }
        }
    }

    public void addCommitStats(CommitFile file, String language) {
        commitTotal += 1;
        lineAdditions += file.getAdditions();
        lineDeletions += file.getDeletions();
        Integer oldAdditions = 0;
        Integer oldDeletions = 0;
        CommitStats languageStats = languageTotalsMap.get(language);
        if (languageStats == null) {
            languageStats = new CommitStats();
        }

        languageStats.setAdditions(languageStats.getAdditions() + file.getAdditions());
        languageStats.setDeletions(languageStats.getDeletions() + file.getDeletions());
        languageTotalsMap.put(language, languageStats);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(repo.getName()).append(" RepositoryStats {\n");
        output.append("\tTotal commits: ").append(commitTotal);
        output.append("\n\tTotal line additions: ").append(lineAdditions);
        output.append("\n\tTotal line deletions: ").append(lineDeletions);
        output.append("\n\tTotal Languages: ").append(languageTotalsMap.keySet().size());
        output.append("\n\tLanguage Totals {");
        for (String language : languageTotalsMap.keySet()) {
            CommitStats languageStats = languageTotalsMap.get(language);
            output.append("\n\t\t").append(language).append(" {");
            output.append("\n\t\t\tAdditions: ").append(languageStats.getAdditions());
            output.append("\n\t\t\tDeletions: ").append(languageStats.getDeletions());
            output.append("\n\t\t}");
        }
        output.append("\n\t}");
        output.append("\n}");
        return output.toString();
    }
}