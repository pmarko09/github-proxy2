package com.github_proxy2.github_proxy2.service;

import com.github_proxy2.github_proxy2.client.GithubProxyClient;
import com.github_proxy2.github_proxy2.exception.RepositoryAlreadyExistsException;
import com.github_proxy2.github_proxy2.exception.RepositoryNotFoundException;
import com.github_proxy2.github_proxy2.mapper.MyRepoMapper;
import com.github_proxy2.github_proxy2.model.dto.GithubProxyDto;
import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import com.github_proxy2.github_proxy2.model.entity.Owner;
import com.github_proxy2.github_proxy2.repository.MyRepoRepository;
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
    MyRepoRepository myRepoRepository;
    MyRepoMapper mapper;
    GithubProxyService service;

    @BeforeEach
    void setup() {
        this.githubProxyClient = Mockito.mock(GithubProxyClient.class);
        this.myRepoRepository = Mockito.mock(MyRepoRepository.class);
        this.mapper = Mappers.getMapper(MyRepoMapper.class);
        this.service = new GithubProxyService(githubProxyClient, myRepoRepository,
                mapper);
    }

    @Test
    void getRepoFromGithub_DataCorrect_MyRepoDtoReturned() {
        Owner owner = new Owner("axa");
        GithubProxyDto dto = new GithubProxyDto("a", "aa", "aaa",
                5, LocalDateTime.of(2020, 12, 12, 12, 12, 12),
                owner, "b");
        when(githubProxyClient.getRepo(owner.getLogin(), "b")).thenReturn(dto);

        MyRepoDto result = service.getRepoFromGithub("axa", "b");

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("aa", result.getDescription());
        assertEquals(5, result.getStars());
        assertEquals("aaa", result.getCloneUrl());
    }

    @Test
    void saveRepoLocally_DataCorrect_MyRepoDtoReturned() {
        Owner owner = new Owner("axa");

        MyRepo myRepo = new MyRepo();
        myRepo.setFullName("a");
        myRepo.setDescription("aa");
        myRepo.setCloneUrl("aaa");
        myRepo.setStars(5);
        myRepo.setOwner("axa");
        myRepo.setRepoName("b");

        GithubProxyDto dto = new GithubProxyDto("a", "aa", "aaa", 5,
                LocalDateTime.of(2020, 12, 12, 12, 12, 12), owner, "b");

        when(githubProxyClient.getRepo("axa", "b")).thenReturn(dto);
        when(myRepoRepository.save(any(MyRepo.class))).thenReturn(myRepo);

        MyRepoDto result = service.saveRepoLocally("axa", "b");

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("aa", result.getDescription());
        assertEquals("aaa", result.getCloneUrl());
        assertEquals(5, result.getStars());
        verify(myRepoRepository).save(any(MyRepo.class));
    }

    @Test
    void saveRepoLocally_RepoAlreadyExists_ThrowException() {
        MyRepo myRepo = new MyRepo();
        myRepo.setFullName("a");
        myRepo.setDescription("aa");
        myRepo.setCloneUrl("aaa");
        myRepo.setStars(5);
        myRepo.setOwner("x");
        myRepo.setRepoName("x1");

        when(myRepoRepository.findByOwnerAndRepoName("x", "x1")).thenReturn(Optional.of(myRepo));

        RepositoryAlreadyExistsException aThrows = assertThrows(RepositoryAlreadyExistsException.class, () ->
                service.saveRepoLocally("x", "x1"));
        assertEquals("Repository already exists locally.", aThrows.getMessage());
    }

    @Test
    void editRepo_DataCorrect_MyRepoDtoReturned() {
        MyRepo myRepo = new MyRepo();
        myRepo.setFullName("a");
        myRepo.setDescription("aa");
        myRepo.setCloneUrl("aaa");
        myRepo.setStars(5);
        myRepo.setOwner("axa");
        myRepo.setRepoName("b");

        MyRepo updated = new MyRepo();
        updated.setFullName("a1");
        updated.setDescription("aa1");
        updated.setCloneUrl("aaa1");
        updated.setStars(51);
        updated.setOwner("axa");
        updated.setRepoName("b");

        MyRepoDto myRepoDto = new MyRepoDto();
        myRepoDto.setFullName("a");
        myRepoDto.setDescription("aa");
        myRepoDto.setCloneUrl("aaa");
        myRepoDto.setStars(5);

        when(myRepoRepository.findByOwnerAndRepoName("axa", "b")).thenReturn(Optional.of(myRepo));
        when(myRepoRepository.save(myRepo)).thenReturn(updated);

        MyRepoDto result = service.editRepo("axa", "b", myRepoDto);

        assertNotNull(result);
        assertEquals("a1", result.getFullName());
        assertEquals("aa1", result.getDescription());
        assertEquals("aaa1", result.getCloneUrl());
        assertEquals(51, result.getStars());
        verify(myRepoRepository).findByOwnerAndRepoName("axa", "b");
        verify(myRepoRepository).save(myRepo);
    }

    @Test
    void editRepo_RepoNotFound_ThrowException() {
        MyRepoDto myRepoDto = new MyRepoDto();
        myRepoDto.setFullName("a");
        myRepoDto.setDescription("aa");
        myRepoDto.setCloneUrl("aaa");
        myRepoDto.setStars(5);

        when(myRepoRepository.findByOwnerAndRepoName("a", "b")).thenReturn(Optional.empty());

        RepositoryNotFoundException aThrows = assertThrows(RepositoryNotFoundException.class, () ->
                service.editRepo("a", "b", myRepoDto));
        assertEquals("Repository not found.", aThrows.getMessage());
    }

    @Test
    void deleteRepo_DataCorrect_Void() {
        MyRepo myRepo = new MyRepo();
        myRepo.setFullName("a");
        myRepo.setDescription("aa");
        myRepo.setCloneUrl("aaa");
        myRepo.setStars(5);
        myRepo.setOwner("axa");
        myRepo.setRepoName("b");

        when(myRepoRepository.findByOwnerAndRepoName("axa", "b")).thenReturn(Optional.of(myRepo));

        service.deleteRepo("axa", "b");

        verify(myRepoRepository).findByOwnerAndRepoName("axa", "b");
        verify(myRepoRepository).delete(myRepo);
    }

    @Test
    void deleteRepo_RepoNotFound_ExceptionThrown() {
        when(myRepoRepository.findByOwnerAndRepoName("axa", "b")).thenReturn(Optional.empty());

        RepositoryNotFoundException aThrows = assertThrows(RepositoryNotFoundException.class, () ->
                service.deleteRepo("axa", "b"));
        assertEquals("Repository not found.", aThrows.getMessage());
    }
}
