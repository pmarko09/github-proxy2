package com.github_proxy2.github_proxy2.service;

import com.github_proxy2.github_proxy2.exception.RepositoryNotFoundException;
import com.github_proxy2.github_proxy2.mapper.MyRepoMapper;
import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import com.github_proxy2.github_proxy2.repository.MyRepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class MyRepoServiceTest {

    MyRepoRepository myRepoRepository;
    MyRepoMapper mapper;
    MyRepoService service;

    @BeforeEach
    void setup() {
        this.myRepoRepository = Mockito.mock(MyRepoRepository.class);
        this.mapper = Mappers.getMapper(MyRepoMapper.class);
        this.service = new MyRepoService(myRepoRepository, mapper);
    }

    @Test
    void getRepoFromLocal_DataCorrect_MyRepoDtoReturned() {
        MyRepo myRepo1 = new MyRepo(1L, "a", "b", "c", 5,
                LocalDateTime.of(2024, 11, 11, 20, 30, 0),
                "abc", "x1");

        when(myRepoRepository.findByOwnerAndRepoName("abc", "x1")).thenReturn(Optional.of(myRepo1));

        MyRepoDto result = service.getRepoFromLocal("abc", "x1");

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("c", result.getCloneUrl());
        assertEquals(5, result.getStars());
        verify(myRepoRepository).findByOwnerAndRepoName("abc", "x1");
    }

    @Test
    void getRepoFromLocal_RepoNotFound_ThrowException() {
        when(myRepoRepository.findByOwnerAndRepoName("abc", "x1")).thenReturn(Optional.empty());

        RepositoryNotFoundException aThrows = assertThrows(RepositoryNotFoundException.class, () ->
                service.getRepoFromLocal("abc", "x1)"));

        assertEquals("Repository not found.", aThrows.getMessage());
    }
}
