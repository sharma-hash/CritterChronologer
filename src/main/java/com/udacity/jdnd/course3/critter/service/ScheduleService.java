package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Schedule> getAllSchedules() {

        return scheduleRepository.findAll();
    }

    public Schedule createSchedule(Schedule schedule, List<Long> petIds, List<Long> employeeIds) {

        List<Employee> employees = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();

        petIds.forEach(pet -> {
            pets.add(petRepository.findById(pet).orElseThrow(EntityNotFoundException::new));
        });

        employeeIds.forEach(employee -> {
            employees.add(employeeRepository.findById(employee).orElseThrow(EntityNotFoundException::new));
        });

        schedule.setPets(pets);
        schedule.setEmployees(employees);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        employees.forEach(thisEmployee -> {
            if (thisEmployee.getSchedules() == null)
                thisEmployee.setSchedules(new ArrayList<>());

            thisEmployee.getSchedules().add(schedule);
        });

        pets.forEach(thisPet -> {
            if (thisPet.getSchedules() == null)
                thisPet.setSchedules(new ArrayList<>());

            thisPet.getSchedules().add(schedule);
        });

        return savedSchedule;
    }

}
