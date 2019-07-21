package com.example.Lab1MS.services;

import com.example.Lab1MS.exceptions.BreedNotFoundException;
import com.example.Lab1MS.exceptions.ValidationClass;
import com.example.Lab1MS.exceptions.ValidationException;
import com.example.Lab1MS.models.Breed;
import com.example.Lab1MS.repositories.BreedRepository;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreedServiceImpl implements BreedService {

    @Autowired
    private BreedRepository breedRepository;
    private ValidationClass validationClass = new ValidationClass();

    @Override
    public List<Breed> listAllBreeds() {
        return breedRepository.findAll().stream().filter(x -> !x.getDeleted()).collect(Collectors.toList());
    }

    @Override
    public Breed getBreedById(Long id) {
        var employee = breedRepository.findById(id);
        if(employee.isPresent()){
            if(!employee.get().getDeleted()) return employee.get();
            else throw new BreedNotFoundException(id);
        }
        else throw new BreedNotFoundException(id);
    }

    @Override
    public Breed saveBreed(Breed breed) {
        List<String> validateMessages = validationClass.validate(breed);
        if (validateMessages.size() > 0) {
           throw new ValidationException(validateMessages);
        } else {
            return breedRepository.save(breed);
        }
    }

    @Override
    public void deleteBreed(Long id) {
        var employee = getBreedById(id);
        employee.setDeleted(true);
        saveBreed(employee);
    }

    @Override
    public Breed updateBreed(Breed newBreed, Long id) {
        List<String> validateMessages = validationClass.validate(newBreed);
        if (validateMessages.size() > 0) {
            throw new ValidationException(validateMessages);
        } else {
            var employee = breedRepository.findById(id);
            if(employee.isPresent()){
                if(!employee.get().getDeleted()){
                    employee.get().setName(newBreed.getName());
                    employee.get().setHairLong(newBreed.getHairLong());
                    return breedRepository.save(employee.get());
                }
                else throw new BreedNotFoundException(id);
            }
            else{
                newBreed.setBreed_id(id);
                return breedRepository.save(newBreed);
            }
        }
    }
}
