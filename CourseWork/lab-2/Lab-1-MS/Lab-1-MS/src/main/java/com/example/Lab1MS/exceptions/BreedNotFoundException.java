package com.example.Lab1MS.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BreedNotFoundException extends RuntimeException {

    public BreedNotFoundException(Long id) {
        super(String.format("Breed %d is either deleted or doesn't exist", id));
    }
}
