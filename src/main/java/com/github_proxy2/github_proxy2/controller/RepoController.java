package com.github_proxy2.github_proxy2.controller;

import com.github_proxy2.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.github_proxy2.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("local/repositories")
@RequiredArgsConstructor
public class RepoController {

    private final RepoService repoService;

    @GetMapping("/{owner}/{repository-name}")
    public RepoDto getRepoLocally(@PathVariable("owner") String owner,
                                  @PathVariable("repository-name") String repo) {
        return repoService.getRepoFromLocal(owner, repo);
    }
}
