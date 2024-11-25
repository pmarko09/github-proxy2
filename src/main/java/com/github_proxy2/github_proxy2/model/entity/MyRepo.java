package com.github_proxy2.github_proxy2.model.entity;

import com.github_proxy2.github_proxy2.model.dto.GithubProxyDto;
import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
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
public class MyRepo {

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

    public static MyRepo createMyRepo(GithubProxyDto dto) {
        MyRepo myRepo = new MyRepo();
        myRepo.setFullName(dto.getFullName());
        myRepo.setDescription(dto.getDescription());
        myRepo.setCloneUrl(dto.getCloneUrl());
        myRepo.setStars(dto.getStars());
        myRepo.setCreatedAt(dto.getCreatedAt());
        myRepo.setOwner(dto.getOwner().getLogin());
        myRepo.setRepoName(dto.getRepoName());
        return myRepo;
    }

    public static void edit(MyRepo myRepo, MyRepoDto editedRepo) {
        myRepo.setFullName(editedRepo.getFullName());
        myRepo.setDescription(editedRepo.getDescription());
        myRepo.setCloneUrl(editedRepo.getCloneUrl());
        myRepo.setStars(editedRepo.getStars());
        myRepo.setCreatedAt(editedRepo.getCreatedAt());
    }
}
