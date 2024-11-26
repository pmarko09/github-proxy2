package com.github_proxy2.github_proxy2.service;

import com.github_proxy2.github_proxy2.exception.RepositoryNotFoundException;
import com.github_proxy2.github_proxy2.mapper.RepoMapper;
import com.github_proxy2.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.github_proxy2.model.entity.Repo;
import com.github_proxy2.github_proxy2.repository.RepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class RepoServiceTest {

    RepoRepository repoRepository;
    RepoMapper mapper;
    RepoService service;

    @BeforeEach
    void setup() {
        this.repoRepository = Mockito.mock(RepoRepository.class);
        this.mapper = Mappers.getMapper(RepoMapper.class);
        this.service = new RepoService(repoRepository, mapper);
    }

    @Test
    void getRepoFromLocal_DataCorrect_MyRepoDtoReturned() {
        Repo repo1 = new Repo(1L, "a", "b", "c", 5,
                LocalDateTime.of(2024, 11, 11, 20, 30, 0),
                "abc", "x1");

        when(repoRepository.findByOwnerAndRepoName("abc", "x1")).thenReturn(Optional.of(repo1));

        RepoDto result = service.getRepoFromLocal("abc", "x1");

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("c", result.getCloneUrl());
        assertEquals(5, result.getStars());
        verify(repoRepository).findByOwnerAndRepoName("abc", "x1");
    }

    @Test
    void getRepoFromLocal_RepoNotFound_ThrowException() {
        when(repoRepository.findByOwnerAndRepoName("abc", "x1")).thenReturn(Optional.empty());

        RepositoryNotFoundException aThrows = assertThrows(RepositoryNotFoundException.class, () ->
                service.getRepoFromLocal("abc", "x1)"));

        assertEquals("Repository not found.", aThrows.getMessage());
    }
}
