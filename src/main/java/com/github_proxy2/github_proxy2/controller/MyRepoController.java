package com.github_proxy2.github_proxy2.controller;

import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.service.MyRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("local/repositories")
@RequiredArgsConstructor
public class MyRepoController {

    private final MyRepoService myRepoService;

    @GetMapping("/{owner}/{repository-name}")
    public MyRepoDto getRepoLocally(@PathVariable("owner") String owner,
                                    @PathVariable("repository-name") String repo) {
        return myRepoService.getRepoFromLocal(owner, repo);
    }
}
