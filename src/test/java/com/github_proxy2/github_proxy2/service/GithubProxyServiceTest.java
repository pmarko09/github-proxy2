package com.github_proxy2.github_proxy2.service;

import com.github_proxy2.github_proxy2.client.GithubProxyClient;
import com.github_proxy2.github_proxy2.exception.RepositoryAlreadyExistsException;
import com.github_proxy2.github_proxy2.exception.RepositoryNotFoundException;
import com.github_proxy2.github_proxy2.mapper.RepoMapper;
import com.github_proxy2.github_proxy2.model.dto.GithubRepositoryDto;
import com.github_proxy2.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.github_proxy2.model.entity.Repo;
import com.github_proxy2.github_proxy2.model.entity.Owner;
import com.github_proxy2.github_proxy2.repository.RepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class GithubProxyServiceTest {

    GithubProxyClient githubProxyClient;
    RepoRepository repoRepository;
    RepoMapper mapper;
    GithubProxyService service;

    @BeforeEach
    void setup() {
        this.githubProxyClient = Mockito.mock(GithubProxyClient.class);
        this.repoRepository = Mockito.mock(RepoRepository.class);
        this.mapper = Mappers.getMapper(RepoMapper.class);
        this.service = new GithubProxyService(githubProxyClient, repoRepository,
                mapper);
    }

    @Test
    void getRepoFromGithub_DataCorrect_MyRepoDtoReturned() {
        Owner owner = new Owner("axa");
        GithubRepositoryDto dto = new GithubRepositoryDto("a", "aa", "aaa",
                5, LocalDateTime.of(2020, 12, 12, 12, 12, 12),
                owner, "b");
        when(githubProxyClient.getRepo(owner.getLogin(), "b")).thenReturn(dto);

        RepoDto result = service.getRepoFromGithub("axa", "b");

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("aa", result.getDescription());
        assertEquals(5, result.getStars());
        assertEquals("aaa", result.getCloneUrl());
    }

    @Test
    void saveRepoLocally_DataCorrect_MyRepoDtoReturned() {
        Owner owner = new Owner("axa");

        Repo repo = new Repo();
        repo.setFullName("a");
        repo.setDescription("aa");
        repo.setCloneUrl("aaa");
        repo.setStars(5);
        repo.setOwner("axa");
        repo.setRepoName("b");

        GithubRepositoryDto dto = new GithubRepositoryDto("a", "aa", "aaa", 5,
                LocalDateTime.of(2020, 12, 12, 12, 12, 12), owner, "b");

        when(githubProxyClient.getRepo("axa", "b")).thenReturn(dto);
        when(repoRepository.save(any(Repo.class))).thenReturn(repo);

        RepoDto result = service.saveRepoLocally("axa", "b");

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("aa", result.getDescription());
        assertEquals("aaa", result.getCloneUrl());
        assertEquals(5, result.getStars());
        verify(repoRepository).save(any(Repo.class));
    }

    @Test
    void saveRepoLocally_RepoAlreadyExists_ThrowException() {
        Repo repo = new Repo();
        repo.setFullName("a");
        repo.setDescription("aa");
        repo.setCloneUrl("aaa");
        repo.setStars(5);
        repo.setOwner("x");
        repo.setRepoName("x1");

        when(repoRepository.findByOwnerAndRepoName("x", "x1")).thenReturn(Optional.of(repo));

        RepositoryAlreadyExistsException aThrows = assertThrows(RepositoryAlreadyExistsException.class, () ->
                service.saveRepoLocally("x", "x1"));
        assertEquals("Repository already exists locally.", aThrows.getMessage());
    }

    @Test
    void editRepo_DataCorrect_MyRepoDtoReturned() {
        Repo repo = new Repo();
        repo.setFullName("a");
        repo.setDescription("aa");
        repo.setCloneUrl("aaa");
        repo.setStars(5);
        repo.setOwner("axa");
        repo.setRepoName("b");

        Repo updated = new Repo();
        updated.setFullName("a1");
        updated.setDescription("aa1");
        updated.setCloneUrl("aaa1");
        updated.setStars(51);
        updated.setOwner("axa");
        updated.setRepoName("b");

        RepoDto repoDto = new RepoDto();
        repoDto.setFullName("a");
        repoDto.setDescription("aa");
        repoDto.setCloneUrl("aaa");
        repoDto.setStars(5);

        when(repoRepository.findByOwnerAndRepoName("axa", "b")).thenReturn(Optional.of(repo));
        when(repoRepository.save(repo)).thenReturn(updated);

        RepoDto result = service.editRepo("axa", "b", repoDto);

        assertNotNull(result);
        assertEquals("a1", result.getFullName());
        assertEquals("aa1", result.getDescription());
        assertEquals("aaa1", result.getCloneUrl());
        assertEquals(51, result.getStars());
        verify(repoRepository).findByOwnerAndRepoName("axa", "b");
        verify(repoRepository).save(repo);
    }

    @Test
    void editRepo_RepoNotFound_ThrowException() {
        RepoDto repoDto = new RepoDto();
        repoDto.setFullName("a");
        repoDto.setDescription("aa");
        repoDto.setCloneUrl("aaa");
        repoDto.setStars(5);

        when(repoRepository.findByOwnerAndRepoName("a", "b")).thenReturn(Optional.empty());

        RepositoryNotFoundException aThrows = assertThrows(RepositoryNotFoundException.class, () ->
                service.editRepo("a", "b", repoDto));
        assertEquals("Repository not found.", aThrows.getMessage());
    }

    @Test
    void deleteRepo_DataCorrect_Void() {
        Repo repo = new Repo();
        repo.setFullName("a");
        repo.setDescription("aa");
        repo.setCloneUrl("aaa");
        repo.setStars(5);
        repo.setOwner("axa");
        repo.setRepoName("b");

        when(repoRepository.findByOwnerAndRepoName("axa", "b")).thenReturn(Optional.of(repo));

        service.deleteRepo("axa", "b");

        verify(repoRepository).findByOwnerAndRepoName("axa", "b");
        verify(repoRepository).delete(repo);
    }

    @Test
    void deleteRepo_RepoNotFound_ExceptionThrown() {
        when(repoRepository.findByOwnerAndRepoName("axa", "b")).thenReturn(Optional.empty());

        RepositoryNotFoundException aThrows = assertThrows(RepositoryNotFoundException.class, () ->
                service.deleteRepo("axa", "b"));
        assertEquals("Repository not found.", aThrows.getMessage());
    }
}
