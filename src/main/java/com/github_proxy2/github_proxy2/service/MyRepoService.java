package com.github_proxy2.github_proxy2.service;

import com.github_proxy2.github_proxy2.mapper.MyRepoMapper;
import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import com.github_proxy2.github_proxy2.repository.MyRepoRepository;
import com.github_proxy2.github_proxy2.validation.RepositoryValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyRepoService {

    private final MyRepoRepository myRepoRepository;
    private final MyRepoMapper mapper;

    public MyRepoDto getRepoFromLocal(String owner, String repo) {
        MyRepo myRepo = RepositoryValidation.repoFound(myRepoRepository, owner, repo);
        return mapper.toDto(myRepo);
    }
}
