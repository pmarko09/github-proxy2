package com.github_proxy2.github_proxy2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github_proxy2.github_proxy2.client.GithubProxyClient;
import com.github_proxy2.github_proxy2.model.dto.GithubProxyDto;
import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.model.entity.Owner;
import com.github_proxy2.github_proxy2.repository.MyRepoRepository;
import com.github_proxy2.github_proxy2.service.GithubProxyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubProxyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    GithubProxyService service;

    @Mock
    GithubProxyClient proxyClient;

    @InjectMocks
    GithubProxyController controller;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getRepoFromGithub_DataCorrect_ReturnedStatus200() throws Exception {
        MyRepoDto myRepoDto = new MyRepoDto("pmarko09/school-supervision",
                null, "https://github.com/pmarko09/school-supervision.git", 0,
                LocalDateTime.of(2024, 9, 22, 16, 39, 38));

        when(service.getRepoFromGithub("pmarko09", "school-supervision")).thenReturn(myRepoDto);

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

//    @Test
//    void saveRepoLocally_DataCorrect_ReturnedStatus200() throws Exception {
//        MyRepoDto myRepoDto = new MyRepoDto("pmarko09/Try.", "b",
//                "https://github.com/pmarko/Try.", 0,
//                LocalDateTime.of(2020, 10, 10, 10, 10, 10));
//
//        Owner owner = new Owner("abcd");
//
//        GithubProxyDto proxyDto = new GithubProxyDto("pmarko09/Try.", "b",
//                "https://github.com/pmarko/Try.", 5,
//                LocalDateTime.of(2020, 10, 10, 10, 10, 10),
//                owner, "x");
//
//        when(proxyClient.getRepo("pmarko09", "Try.")).thenReturn(proxyDto);
//        when(service.saveRepoLocally("pmarko09", "Try.")).thenReturn(myRepoDto);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/repositories/pmarko09/Try.")
//                ).andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.fullName").value("pmarko09/Try."))
//                .andExpect(jsonPath("$.description").value("b"))
//                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/pmarko09/Try."))
//                .andExpect(jsonPath("$.stars").value(0))
//                .andExpect(jsonPath("$.createdAt").value("2020-10-10T10:10:10"
//                ));
//    }
}
