package com.example.Lab1MS.services;

import com.example.Lab1MS.models.Breed;

import java.util.List;

public interface BreedService {
    List<Breed> listAllBreeds();

    Breed getBreedById(Long id);

    Breed saveBreed(Breed breed);

    void deleteBreed(Long id);

    Breed updateBreed(Breed newBreed, Long id);
}
