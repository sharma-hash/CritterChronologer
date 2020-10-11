package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CustomerService customerService;

    public Pet savePet(Pet pet, Long customerId) {
        if (customerId != null) {
            pet.setCustomer(customerService.getCustomer(customerId));
        }

        pet = petRepository.save(pet);
        customerService.getCustomer(customerId).getPets().add(pet);
        return pet;
    }

    public Pet getPet(long petId) {
        return petRepository.findById(petId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet findById(long id) {
        return petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found in ID : " + id));
    }

}
