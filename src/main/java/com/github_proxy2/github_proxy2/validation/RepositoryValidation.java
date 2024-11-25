package com.github_proxy2.github_proxy2.validation;

import com.github_proxy2.github_proxy2.exception.RepositoryAlreadyExistsException;
import com.github_proxy2.github_proxy2.exception.RepositoryNotFoundException;
import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import com.github_proxy2.github_proxy2.repository.MyRepoRepository;

public class RepositoryValidation {

    public static void repoExistsLocally(MyRepoRepository myRepo, String owner, String repoName) {

        myRepo.findByOwnerAndRepoName(owner, repoName).ifPresent(repo -> {
            throw new RepositoryAlreadyExistsException();
        });
    }

    public static MyRepo repoFound(MyRepoRepository myRepo, String owner, String repoName) {
        return myRepo.findByOwnerAndRepoName(owner, repoName)
                .orElseThrow(RepositoryNotFoundException::new);
    }
}
