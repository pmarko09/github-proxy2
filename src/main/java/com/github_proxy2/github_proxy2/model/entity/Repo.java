package com.github_proxy2.github_proxy2.model.entity;

import com.github_proxy2.github_proxy2.model.dto.GithubRepositoryDto;
import com.github_proxy2.github_proxy2.model.dto.RepoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPOSITORIES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    private String description;

    @Column(name = "clone_url")
    private String cloneUrl;

    private int stars;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "owner")
    private String owner;

    @Column(name = "repo_name")
    private String repoName;

    public static Repo createMyRepo(GithubRepositoryDto dto) {
        Repo repo = new Repo();
        repo.setFullName(dto.getFullName());
        repo.setDescription(dto.getDescription());
        repo.setCloneUrl(dto.getCloneUrl());
        repo.setStars(dto.getStars());
        repo.setCreatedAt(dto.getCreatedAt());
        repo.setOwner(dto.getOwner().getLogin());
        repo.setRepoName(dto.getRepoName());
        return repo;
    }

    public static void edit(Repo repo, RepoDto editedRepo) {
        repo.setFullName(editedRepo.getFullName());
        repo.setDescription(editedRepo.getDescription());
        repo.setCloneUrl(editedRepo.getCloneUrl());
        repo.setStars(editedRepo.getStars());
        repo.setCreatedAt(editedRepo.getCreatedAt());
    }
}
