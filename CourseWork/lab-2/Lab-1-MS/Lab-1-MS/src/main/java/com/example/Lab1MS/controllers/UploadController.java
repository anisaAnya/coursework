package com.example.Lab1MS.controllers;

import com.example.Lab1MS.assemblers.BreedResourceAssembler;
import com.example.Lab1MS.repositories.BreedRepository;
import com.example.Lab1MS.services.BreedService;
import com.example.Lab1MS.services.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class UploadController {
    private static Logger log = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private BreedService breedService;

    @Autowired
    private StorageService storageService;

     private final BreedRepository repositoryEmployee;
    private final BreedResourceAssembler assemblerBreed;

    public UploadController(BreedRepository repositoryBreed,
                            BreedResourceAssembler assemblerBreed) {

        this.assemblerBreed = assemblerBreed;
        this.repositoryEmployee = repositoryBreed;
    }

    @GetMapping(value = "/tuk")
    public String getMew() {

        return "mew";
    }

    @PostMapping("/api/Upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        log.info(file.getContentType());
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        log.info("wse good");
        return "vse good";
    }

}