package com.github_proxy2.github_proxy2.mapper;

import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MyRepoMapperTest {

    MyRepoMapper mapper = Mappers.getMapper(MyRepoMapper.class);

    @Test
    void mapMyRepoToDto() {

        MyRepo myRepo = new MyRepo(1L, "a", "aa", "aaa", 5,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                "b", "bb");

        MyRepoDto result = mapper.toDto(myRepo);

        assertNotNull(result);
        assertEquals("a", result.getFullName());
        assertEquals("aa", result.getDescription());
        assertEquals("aaa", result.getCloneUrl());
    }
}
