package com.example.Lab1MS.loaders;
import com.example.Lab1MS.models.Breed;
import com.example.Lab1MS.repositories.BreedRepository;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabaseEmployee {

    @Bean
    public CommandLineRunner initDatabaseEmployee(BreedRepository repository) {
        return args -> {
            var checkList = repository.findAll();
            if (checkList.isEmpty()){
                log.info("Preloading breed " + repository.save(new Breed("British", "Short", "Rare")));
                log.info("Preloading breed " + repository.save(new Breed("Sphinx", "bold", "Rare")));
            }
            else {
                log.info("Current rows in table breeds: ");
                repository.findAll().forEach(x -> log.info(x.getBreed_id() + ": " + x.getName() + " - " + x.getHairLong()));
            }
        };
    }
}
