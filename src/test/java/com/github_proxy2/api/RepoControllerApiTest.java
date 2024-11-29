package com.github_proxy2.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.model.entity.Repo;
import com.github_proxy2.repository.RepoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWireMock(port = 8089)
public class RepoControllerApiTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RepoRepository repoRepository;

    private static final String OWNER = "ABC";
    private static final String REPO_NAME = "1234";

    @Test
    void getRepoLocally_DataCorrect_RepoDtoReturned() throws JsonProcessingException {
        Repo savedRepo = new Repo(
                1L,
                "pmarko09/school-supervision",
                "description",
                "https://github.com/pmarko09/school-supervision.git",
                0,
                LocalDateTime.of(2024, 9, 22, 16, 39, 38),
                OWNER,
                REPO_NAME
        );
        repoRepository.save(savedRepo);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8083")
                .path("/local/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<RepoDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                RepoDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RepoDto result = response.getBody();
        assertNotNull(result);
        assertEquals(savedRepo.getFullName(), result.getFullName());
        assertEquals(savedRepo.getDescription(), result.getDescription());
        assertEquals(savedRepo.getCloneUrl(), result.getCloneUrl());
        assertEquals(savedRepo.getStars(), result.getStars());
        assertEquals(savedRepo.getCreatedAt(), result.getCreatedAt());
    }
}
