package com.github_proxy2.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github_proxy2.model.dto.RepoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWireMock(port = 8089)
public class RepoControllerApiTest {

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper mapper;

    private static final String OWNER = "ABC";
    private static final String REPO_NAME = "1234";

    private static final RepoDto REPO_DTO = new RepoDto("pmarko09/school-supervision",
            null, "https://github.com/pmarko09/school-supervision.git", 0,
            LocalDateTime.of(2024, 9, 22, 16, 39, 38));

    @Test
    void getRepoLocally_DataCorrect_RepoDtoReturned() throws JsonProcessingException {

        wireMockServer.stubFor(WireMock.get("/local/repositories/"
                        + OWNER + "/" + REPO_NAME)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(REPO_DTO))));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8089")
                .path("/local/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<RepoDto> response = restTemplate.getForEntity(url, RepoDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        RepoDto result = response.getBody();
        assertNotNull(result);
        assertEquals(REPO_DTO.getFullName(), result.getFullName());
        assertEquals(REPO_DTO.getDescription(), result.getDescription());
        assertEquals(REPO_DTO.getStars(), result.getStars());
        assertEquals(REPO_DTO.getCloneUrl(), result.getCloneUrl());
        assertEquals(REPO_DTO.getCreatedAt(), result.getCreatedAt());
    }
}
