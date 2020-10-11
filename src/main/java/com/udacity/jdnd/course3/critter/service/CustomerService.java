package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(EntityNotFoundException::new);
    }

    public Customer saveCustomer(Customer customer, List<Long> petIds) {
        List<Pet> pets = new ArrayList<>();
        if (petIds != null && !petIds.isEmpty()) {
            pets = petIds.stream().map((petId) -> petRepository.getOne(petId)).collect(Collectors.toList());
        }
        customer.setPets(pets);
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public Customer findById(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not found in Id : " + customerId));

    }


}
