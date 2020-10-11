package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

   @Autowired
   PetService petService;

   @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return setPetDTO(petService.savePet(pet, petDTO.getOwnerId()));
    }

    @PostMapping("/{ownerId}")
    public PetDTO savePet(@RequestBody PetDTO petDTO, @PathVariable("ownerId") Long ownerId) {
        petDTO.setOwnerId(ownerId);
        return savePet(petDTO);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return setPetDTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        return pets.stream().map(this::setPetDTO).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {

        Customer customer = customerService.findById(ownerId);
        List<Pet> customerPets = customer.getPets();
        return customerPets.stream().map(this::setPetDTO).collect(Collectors.toList());
    }

    private PetDTO setPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getCustomer() != null) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }
}
