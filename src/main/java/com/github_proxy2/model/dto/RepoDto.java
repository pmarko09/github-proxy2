package com.github_proxy2.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RepoDto {

    private String fullName;

    private String description;

    private String cloneUrl;

    private int stars;

    private LocalDateTime createdAt;

    public static RepoDto create(GithubRepositoryDto githubDto) {
        RepoDto repoDto = new RepoDto();
        repoDto.setFullName(githubDto.getFullName());
        repoDto.setDescription(githubDto.getDescription());
        repoDto.setCloneUrl(githubDto.getCloneUrl());
        repoDto.setStars(githubDto.getStars());
        repoDto.setCreatedAt(githubDto.getCreatedAt());
        return repoDto;
    }
}
