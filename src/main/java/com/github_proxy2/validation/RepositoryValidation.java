package com.github_proxy2.validation;

import com.github_proxy2.exception.RepositoryAlreadyExistsException;
import com.github_proxy2.exception.RepositoryNotFoundException;
import com.github_proxy2.model.entity.Repo;
import com.github_proxy2.repository.RepoRepository;

public class RepositoryValidation {

    public static void repoExistsLocally(RepoRepository myRepo, String owner, String repoName) {

        myRepo.findByOwnerAndRepoName(owner, repoName).ifPresent(repo -> {
            throw new RepositoryAlreadyExistsException();
        });
    }

    public static Repo repoFound(RepoRepository myRepo, String owner, String repoName) {
        return myRepo.findByOwnerAndRepoName(owner, repoName)
                .orElseThrow(RepositoryNotFoundException::new);
    }
}
