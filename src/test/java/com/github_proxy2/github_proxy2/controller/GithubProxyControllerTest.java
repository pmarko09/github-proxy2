package com.github_proxy2.github_proxy2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github_proxy2.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.github_proxy2.service.GithubProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubProxyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GithubProxyService service;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getRepoFromGithub_DataCorrect_ReturnedStatus200() throws Exception {
        RepoDto repoDto = new RepoDto("pmarko09/school-supervision",
                null, "https://github.com/pmarko09/school-supervision.git", 0,
                LocalDateTime.of(2024, 9, 22, 16, 39, 38));

        when(service.getRepoFromGithub("pmarko09", "school-supervision")).thenReturn(repoDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/repositories/pmarko09/school-supervision")
                ).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.fullName").value("pmarko09/school-supervision"))
                .andExpect(jsonPath("$.description").value(nullValue()))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/pmarko09/school-supervision.git"))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("2024-09-22T16:39:38"
                ));
    }

    @Test
    void saveRepoLocally_DataCorrect_ReturnedStatus200() throws Exception {
        RepoDto repoDto = new RepoDto("pmarko09/Try.", "b",
                "https://github.com/pmarko09/Try.", 0,
                LocalDateTime.of(2020, 10, 10, 10, 10, 10));

        when(service.saveRepoLocally("pmarko09", "Try.")).thenReturn(repoDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/repositories/pmarko09/Try.")
                ).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.fullName").value("pmarko09/Try."))
                .andExpect(jsonPath("$.description").value("b"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/pmarko09/Try."))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("2020-10-10T10:10:10"
                ));
    }

    @Test
    void editRepoLocally_DataCorrect_ReturnedStatus200() throws Exception {
        RepoDto updated = new RepoDto("a1", "a2",
                "google", 99,
                LocalDateTime.of(2024, 1, 1, 1, 1, 1));

        when(service.editRepo("xxx", "abc", updated)).thenReturn(updated);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/repositories/xxx/abc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updated))
                ).andDo(print())
                .andExpect(jsonPath("$.fullName").value("a1"))
                .andExpect(jsonPath("$.description").value("a2"))
                .andExpect(jsonPath("$.cloneUrl").value("google"))
                .andExpect(jsonPath("$.stars").value(99))
                .andExpect(jsonPath("$.createdAt").value("2024-01-01T01:01:01"));
    }

    @Test
    void deleteRepo_DataCorrect_ReturnedStatus200() throws Exception {
        doNothing().when(service).deleteRepo("xx", "ab");

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/repositories/xx/ab")
                ).andDo(print())
                .andExpect(status().isOk());
    }
}
