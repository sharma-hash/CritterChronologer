package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getEmployee(long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EntityNotFoundException::new);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> getEmployeesForService(Set<EmployeeSkill> skills, DayOfWeek dayOfWeek) {

        List<Employee> availableEmployees = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAllBySkillsInAndDaysAvailableContains(skills, dayOfWeek);

        employees.forEach(employee -> {
            if(employee.getSkills().containsAll(skills)){ availableEmployees.add(employee); }
        });

        return availableEmployees;
    }
}
