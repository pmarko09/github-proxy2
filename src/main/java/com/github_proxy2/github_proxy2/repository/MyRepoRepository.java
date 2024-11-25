package com.github_proxy2.github_proxy2.repository;

import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyRepoRepository extends JpaRepository<MyRepo, Long> {

    @Query("SELECT r FROM MyRepo r WHERE r.owner = :owner AND r.repoName = :repoName")
    Optional<MyRepo> findByOwnerAndRepoName(@Param("owner") String owner, @Param("repoName") String repoName);
}
