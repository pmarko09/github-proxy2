package com.github_proxy2.service;

import com.github_proxy2.client.GithubProxyClient;
import com.github_proxy2.mapper.RepoMapper;
import com.github_proxy2.model.dto.GithubRepositoryDto;
import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.model.entity.Repo;
import com.github_proxy2.repository.RepoRepository;
import com.github_proxy2.validation.RepositoryValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GithubProxyService {

    private final GithubProxyClient githubProxyClient;
    private final RepoRepository repoRepository;
    private final RepoMapper mapper;

    public RepoDto getRepoFromGithub(String owner, String repo) {
        GithubRepositoryDto response = githubProxyClient.getRepo(owner, repo);

        return RepoDto.create(response);
    }

    @Transactional
    public RepoDto saveRepoLocally(String owner, String repo) {
        GithubRepositoryDto response = githubProxyClient.getRepo(owner, repo);

        RepositoryValidation.repoExistsLocally(repoRepository, owner, repo);
        Repo myRepo = Repo.createMyRepo(response);
        Repo savedRepo = repoRepository.save(myRepo);

        return mapper.toDto(savedRepo);
    }

    @Transactional
    public RepoDto editRepo(String owner, String repo, RepoDto editedRepo) {
        Repo existingRepo = RepositoryValidation.repoFound(repoRepository, owner, repo);
        Repo.edit(existingRepo, editedRepo);
        return mapper.toDto(repoRepository.save(existingRepo));
    }

    @Transactional
    public void deleteRepo(String owner, String repo) {
        Repo existingRepo = RepositoryValidation.repoFound(repoRepository, owner, repo);
        repoRepository.delete(existingRepo);
    }
}
