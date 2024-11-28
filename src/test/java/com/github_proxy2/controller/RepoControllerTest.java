package com.github_proxy2.controller;

import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.service.RepoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class RepoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RepoService service;

    @Test
    void getRepoLocally_DataCorrect_ReturnedStatus200() throws Exception {
        RepoDto repoDto = new RepoDto("a1", "a2",
                "google", 99,
                LocalDateTime.of(2024, 1, 1, 1, 1, 1));

        when(service.getRepoFromLocal("a", "b")).thenReturn(repoDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/local/repositories/a/b")
                ).andDo(print())
                .andExpect(jsonPath("$.fullName").value("a1"))
                .andExpect(jsonPath("$.description").value("a2"))
                .andExpect(jsonPath("$.cloneUrl").value("google"))
                .andExpect(jsonPath("$.stars").value(99))
                .andExpect(jsonPath("$.createdAt").value("2024-01-01T01:01:01"));
    }
}


