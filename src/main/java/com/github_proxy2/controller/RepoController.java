package com.github_proxy2.controller;

import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/local/repositories")
@RequiredArgsConstructor
public class RepoController {

    private final RepoService repoService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{owner}/{repository-name}")
    public RepoDto getRepoLocally(@PathVariable("owner") String owner,
                                  @PathVariable("repository-name") String repo) {
        return repoService.getRepoFromLocal(owner, repo);
    }
}
