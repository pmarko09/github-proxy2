package com.github_proxy2.github_proxy2.controller;

import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.service.GithubProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repositories")
@RequiredArgsConstructor
public class GithubProxyController {

    private final GithubProxyService githubRepoService;

    @GetMapping("/{owner}/{repository-name}")
    public MyRepoDto getRepoFromGithub(@PathVariable("owner") String owner,
                                       @PathVariable("repository-name") String repo) {
        return githubRepoService.getRepoFromGithub(owner, repo);
    }

    @PostMapping("/{owner}/{repository-name}")
    public MyRepoDto saveRepoLocally(@PathVariable("owner") String owner,
                                     @PathVariable("repository-name") String repo) {
        return githubRepoService.saveRepoLocally(owner, repo);
    }

    @PutMapping("/{owner}/{repository-name}")
    public MyRepoDto editRepoLocally(@PathVariable("owner") String owner,
                                     @PathVariable("repository-name") String repo,
                                     @RequestBody MyRepoDto editedRepo) {
        return githubRepoService.editRepo(owner, repo, editedRepo);
    }

    @DeleteMapping("/{owner}/{repository-name}")
    public void deleteRepo(@PathVariable("owner") String owner,
                           @PathVariable("repository-name") String repo) {
        githubRepoService.deleteRepo(owner, repo);
    }
}
