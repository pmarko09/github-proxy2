package com.github_proxy2.controller;

import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.service.GithubProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repositories")
@RequiredArgsConstructor
public class GithubProxyController {

    private final GithubProxyService githubRepoService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{owner}/{repository-name}")
    public RepoDto getRepoFromGithub(@PathVariable("owner") String owner,
                                     @PathVariable("repository-name") String repo) {
        return githubRepoService.getRepoFromGithub(owner, repo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{owner}/{repository-name}")
    public RepoDto saveRepoLocally(@PathVariable("owner") String owner,
                                   @PathVariable("repository-name") String repo) {
        return githubRepoService.saveRepoLocally(owner, repo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{owner}/{repository-name}")
    public RepoDto editRepoLocally(@PathVariable("owner") String owner,
                                   @PathVariable("repository-name") String repo,
                                   @RequestBody RepoDto editedRepo) {
        return githubRepoService.editRepo(owner, repo, editedRepo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{owner}/{repository-name}")
    public void deleteRepo(@PathVariable("owner") String owner,
                           @PathVariable("repository-name") String repo) {
        githubRepoService.deleteRepo(owner, repo);
    }
}
