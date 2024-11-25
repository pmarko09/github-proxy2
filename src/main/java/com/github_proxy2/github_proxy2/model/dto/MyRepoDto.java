package com.github_proxy2.github_proxy2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyRepoDto {

    private String fullName;

    private String description;

    private String cloneUrl;

    private int stars;

    private LocalDateTime createdAt;

    public static MyRepoDto create(GithubProxyDto githubDto) {
        MyRepoDto myRepoDto = new MyRepoDto();
        myRepoDto.setFullName(githubDto.getFullName());
        myRepoDto.setDescription(githubDto.getDescription());
        myRepoDto.setCloneUrl(githubDto.getCloneUrl());
        myRepoDto.setStars(githubDto.getStars());
        myRepoDto.setCreatedAt(githubDto.getCreatedAt());
        return myRepoDto;
    }
}
