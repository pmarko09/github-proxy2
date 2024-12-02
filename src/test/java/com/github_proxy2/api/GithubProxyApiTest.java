package com.github_proxy2.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github_proxy2.model.dto.GithubRepositoryDto;
import com.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.model.entity.Owner;
import com.github_proxy2.model.entity.Repo;
import com.github_proxy2.repository.RepoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWireMock(port = 8089)
public class GithubProxyApiTest {

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RepoRepository repoRepository;

    @Autowired
    ObjectMapper mapper;

    private static final String OWNER = "abc";
    private static final String REPO_NAME = "1234";

    private static final GithubRepositoryDto GITHUB_REPO_DTO = new GithubRepositoryDto(
            "pmarko09/school-supervision",
            null, "https://github.com/pmarko09/school-supervision.git", 0,
            LocalDateTime.of(2024, 9, 22, 16, 39, 38),
            new Owner("abc"), "1234");

    @BeforeEach
    public void setup() {
        wireMockServer.start();
        restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8089")
                .build();
        repoRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getRepoFromGithubTest_DataCorrect_RepoDtoReturned() throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(
                        "/repos/" + OWNER + "/" + REPO_NAME))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(GITHUB_REPO_DTO))));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8083")
                .path("/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<RepoDto> response = restTemplate.getForEntity(url, RepoDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RepoDto result = response.getBody();
        assertNotNull(result);
        assertEquals(GITHUB_REPO_DTO.getFullName(), result.getFullName());
        assertEquals(GITHUB_REPO_DTO.getDescription(), result.getDescription());
        assertEquals(GITHUB_REPO_DTO.getStars(), result.getStars());
        assertEquals(GITHUB_REPO_DTO.getCloneUrl(), result.getCloneUrl());
        assertEquals(GITHUB_REPO_DTO.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void saveRepoLocallyTest_DataCorrect_RepoDtoReturned() throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(
                        "/repos/" + OWNER + "/" + REPO_NAME))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(GITHUB_REPO_DTO))));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8083")
                .path("/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<RepoDto> response = restTemplate.postForEntity(url, null, RepoDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RepoDto result = response.getBody();
        assertNotNull(result);

        Optional<Repo> savedRepo = repoRepository.findByOwnerAndRepoName("abc", "1234");
        assertTrue(savedRepo.isPresent(), "Repozytorium nie zostało zapisane w bazie danych.");

        Repo repo = savedRepo.get();
        assertEquals(GITHUB_REPO_DTO.getFullName(), repo.getFullName());
        assertEquals(GITHUB_REPO_DTO.getDescription(), repo.getDescription());
        assertEquals(GITHUB_REPO_DTO.getStars(), repo.getStars());
        assertEquals(GITHUB_REPO_DTO.getCloneUrl(), repo.getCloneUrl());
        assertEquals(GITHUB_REPO_DTO.getCreatedAt(), repo.getCreatedAt());

        assertEquals(result.getFullName(), GITHUB_REPO_DTO.getFullName());
        assertEquals(result.getStars(), GITHUB_REPO_DTO.getStars());
        assertEquals(result.getDescription(), GITHUB_REPO_DTO.getDescription());
        assertEquals(result.getCreatedAt(), GITHUB_REPO_DTO.getCreatedAt());
        assertEquals(result.getCloneUrl(), GITHUB_REPO_DTO.getCloneUrl());
    }

    @Test
    void editRepoLocally_DataCorrect_RepoDtoReturned() throws JsonProcessingException {
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

        RepoDto editedRepo = new RepoDto(
                "change1",
                "change2",
                "change3",
                997,
                LocalDateTime.of(2024, 9, 22, 16, 39, 38)
        );

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8083")
                .path("/repositories/" + OWNER + "/" + REPO_NAME)
                .toUriString();

        ResponseEntity<RepoDto> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(editedRepo),
                RepoDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RepoDto result = response.getBody();

        Optional<Repo> byOwnerAndRepoName = repoRepository.findByOwnerAndRepoName(OWNER, REPO_NAME);
        assertTrue(byOwnerAndRepoName.isPresent());
        assertNotNull(result);
        assertEquals(editedRepo.getFullName(), result.getFullName());
        assertEquals(editedRepo.getDescription(), result.getDescription());
        assertEquals(editedRepo.getStars(), result.getStars());
        assertEquals(editedRepo.getCloneUrl(), result.getCloneUrl());
    }

    @Test
    void deleteRepoTest_DataCorrect_NoContentReturned() throws JsonProcessingException {
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
