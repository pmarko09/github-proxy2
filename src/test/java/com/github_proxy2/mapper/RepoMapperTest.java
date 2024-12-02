package com.github_proxy2.mapper;

import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.model.entity.Repo;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RepoMapperTest {

    RepoMapper mapper = Mappers.getMapper(RepoMapper.class);

    @Test
    void mapMyRepoToDto() {

        Repo repo = new Repo(1L, "a", "aa", "aaa", 5,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                "b", "bb");

        RepoDto result = mapper.toDto(repo);

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("aa", result.getDescription());
        assertEquals("aaa", result.getCloneUrl());
    }
}
