package com.example.Lab1MS.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.example.Lab1MS.controllers.DBController;
import com.example.Lab1MS.models.Breed;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class BreedResourceAssembler implements ResourceAssembler<Breed, Resource<Breed>> {

    @Override
    public Resource<Breed> toResource(Breed breed) {

        return new Resource<>(breed,
                linkTo(methodOn(DBController.class).getEmployee(breed.getBreed_id())).withSelfRel(),
                linkTo(methodOn(DBController.class).getEmployees()).withRel("employees"));
    }
}
