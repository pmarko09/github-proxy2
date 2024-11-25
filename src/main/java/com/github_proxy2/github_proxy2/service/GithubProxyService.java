package com.github_proxy2.github_proxy2.service;

import com.github_proxy2.github_proxy2.client.GithubProxyClient;
import com.github_proxy2.github_proxy2.mapper.MyRepoMapper;
import com.github_proxy2.github_proxy2.model.dto.GithubProxyDto;
import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import com.github_proxy2.github_proxy2.repository.MyRepoRepository;
import com.github_proxy2.github_proxy2.validation.RepositoryValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubProxyService {

    private final GithubProxyClient githubProxyClient;
    private final MyRepoRepository myRepoRepository;
    private final MyRepoMapper mapper;

    public MyRepoDto getRepoFromGithub(String owner, String repo) {
        GithubProxyDto response = githubProxyClient.getRepo(owner, repo);

        return MyRepoDto.create(response);
    }

    public MyRepoDto saveRepoLocally(String owner, String repo) {
        GithubProxyDto response = githubProxyClient.getRepo(owner, repo);

        RepositoryValidation.repoExistsLocally(myRepoRepository, owner, repo);
        MyRepo myRepo = MyRepo.createMyRepo(response);
        MyRepo savedRepo = myRepoRepository.save(myRepo);

        return mapper.toDto(savedRepo);
    }

    public MyRepoDto editRepo(String owner, String repo, MyRepoDto editedRepo) {
        MyRepo existingRepo = RepositoryValidation.repoFound(myRepoRepository, owner, repo);
        MyRepo.edit(existingRepo, editedRepo);
        return mapper.toDto(myRepoRepository.save(existingRepo));
    }

    public void deleteRepo(String owner, String repo) {
        MyRepo existingRepo = RepositoryValidation.repoFound(myRepoRepository, owner, repo);
        myRepoRepository.delete(existingRepo);
    }
}
