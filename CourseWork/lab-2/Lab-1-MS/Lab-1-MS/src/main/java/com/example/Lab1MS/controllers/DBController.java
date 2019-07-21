package com.example.Lab1MS.controllers;

import com.example.Lab1MS.assemblers.BreedResourceAssembler;
import com.example.Lab1MS.models.Breed;
import com.example.Lab1MS.repositories.BreedRepository;
import com.example.Lab1MS.services.BreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class DBController {

    @Autowired
    private BreedService breedService;

    private final BreedRepository repository;
    private final BreedResourceAssembler assembler;

    public DBController(BreedRepository repository,
                           BreedResourceAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping(value = "/breeds", produces = "application/json; charset=UTF-8")
    public Resources<Resource<Breed>> getEmployees() {

        List<Resource<Breed>> employees = breedService.listAllBreeds().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(employees,
                linkTo(methodOn(DBController.class).getEmployees()).withSelfRel());
    }

    @PostMapping(value = "/breeds", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8" )
    public ResponseEntity<?> postEmployee(@RequestBody Breed newBreed) throws URISyntaxException {

        Resource<Breed> resource = assembler.toResource(breedService.saveBreed(newBreed));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }


    @GetMapping(value = "/breeds/{id}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ResourceSupport> getEmployee(@PathVariable Long id) {
        Breed breed = breedService.getBreedById(id);
        return ResponseEntity.ok(assembler.toResource(breed));
    }

    @PutMapping(value = "/breeds/{id}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> replaceEmployee(@RequestBody Breed newBreed, @PathVariable Long id) throws URISyntaxException {
        Breed updatedBreed = breedService.updateBreed(newBreed, id);

        Resource<Breed> resource = assembler.toResource(updatedBreed);

        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping(value = "/breeds/{id}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> deleteEmp(@PathVariable Long id) {
        Breed breed = breedService.getBreedById(id);
        breedService.deleteBreed(id);
        return ResponseEntity.ok().build();
    }
}
