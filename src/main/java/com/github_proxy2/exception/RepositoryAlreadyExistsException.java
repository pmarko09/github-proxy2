package com.github_proxy2.exception;

public class RepositoryAlreadyExistsException extends RuntimeException {
    public RepositoryAlreadyExistsException() {
        super("Repository already exists locally.");
    }

    public RepositoryAlreadyExistsException(String message) {
        super(message);
    }
}
