package com.github_proxy2.github_proxy2.repository;

import com.github_proxy2.github_proxy2.model.entity.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoRepository extends JpaRepository<Repo, Long> {

    @Query("SELECT r FROM Repo r WHERE r.owner = :owner AND r.repoName = :repoName")
    Optional<Repo> findByOwnerAndRepoName(@Param("owner") String owner, @Param("repoName") String repoName);
}
