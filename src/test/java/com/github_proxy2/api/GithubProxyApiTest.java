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
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWireMock(port = 8089)
public class GithubProxyApiTest {

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
    void getRepoFromGithubTest_DataCorrect_RepoDtoReturned() throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(
                        "/repositories/" + OWNER + "/" + REPO_NAME))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(REPO_DTO))));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8089")
                .path("/repositories/" + OWNER + "/" + REPO_NAME)
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

    @Test
    void saveRepoLocallyTest_DataCorrect_RepoDtoReturned() throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(
                        "/repositories/" + OWNER + "/" + REPO_NAME))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(REPO_DTO))));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8089")
                .path("/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<RepoDto> response = restTemplate.postForEntity(url, REPO_DTO, RepoDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        RepoDto result = response.getBody();
        assertNotNull(result);
        assertEquals(result.getFullName(), REPO_DTO.getFullName());
        assertEquals(result.getStars(), REPO_DTO.getStars());
        assertEquals(result.getDescription(), REPO_DTO.getDescription());
        assertEquals(result.getCreatedAt(), REPO_DTO.getCreatedAt());
        assertEquals(result.getCloneUrl(), REPO_DTO.getCloneUrl());
    }

    @Test
    void editRepoLocally_DataCorrect_RepoDtoReturned() throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.put("/repositories/" + OWNER + "/" + REPO_NAME)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(REPO_DTO))));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8089")
                .path("/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<RepoDto> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(REPO_DTO),
                RepoDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        RepoDto result = response.getBody();
        assertNotNull(result);
        assertEquals(result.getFullName(), REPO_DTO.getFullName());
        assertEquals(result.getCloneUrl(), REPO_DTO.getCloneUrl());
        assertEquals(result.getDescription(), REPO_DTO.getDescription());
        assertEquals(result.getStars(), REPO_DTO.getStars());
        assertEquals(result.getCreatedAt(), REPO_DTO.getCreatedAt());
    }

    @Test
    void deleteRepoTest_DataCorrect_NoContentReturned() throws JsonProcessingException {

        wireMockServer.stubFor(WireMock.delete("/repositories/" + OWNER + "/" + REPO_NAME)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(REPO_DTO))));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8089")
                .path("/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
