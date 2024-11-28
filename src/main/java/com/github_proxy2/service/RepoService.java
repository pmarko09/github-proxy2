package com.github_proxy2.service;

import com.github_proxy2.mapper.RepoMapper;
import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.model.entity.Repo;
import com.github_proxy2.repository.RepoRepository;
import com.github_proxy2.validation.RepositoryValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final RepoRepository repoRepository;
    private final RepoMapper mapper;

    public RepoDto getRepoFromLocal(String owner, String repo) {
        Repo myRepo = RepositoryValidation.repoFound(repoRepository, owner, repo);
        return mapper.toDto(myRepo);
    }
}
