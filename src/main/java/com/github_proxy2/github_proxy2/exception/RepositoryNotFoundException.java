package com.github_proxy2.github_proxy2.exception;

public class RepositoryNotFoundException extends RuntimeException {
    public RepositoryNotFoundException() {
        super("Repository not found.");
    }

    public RepositoryNotFoundException(String message) {
        super(message);
    }
}
