package com.github_proxy2.client;

import com.github_proxy2.model.dto.GithubRepositoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "githubClient", url = "${spring.cloud.openfeign.client.config.githubProxyClient.url}")
public interface GithubProxyClient {

    @GetMapping("/repos/{owner}/{repo}")
    GithubRepositoryDto getRepo(
            @PathVariable String owner,
            @PathVariable String repo);
}