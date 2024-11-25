package com.github_proxy2.github_proxy2.client;

import com.github_proxy2.github_proxy2.model.dto.GithubProxyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GithubProxyClient {

    @GetMapping("/repos/{owner}/{repo}")
    GithubProxyDto getRepo(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repositoryName);
}
