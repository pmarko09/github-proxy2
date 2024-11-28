package com.github_proxy2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github_proxy2.model.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepositoryDto {

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    @JsonProperty("clone_url")
    private String cloneUrl;

    @JsonProperty("stargazers_count")
    private int stars;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("owner")
    private Owner owner;

    @JsonProperty("name")
    private String repoName;
}
