package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();

        BeanUtils.copyProperties(scheduleDTO, schedule);

        return setScheduleDTO(scheduleService.createSchedule(schedule,
                scheduleDTO.getPetIds(), scheduleDTO.getEmployeeIds()));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return setScheduleListToScheduleListDTO(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {

        List<Schedule> schedules = petService.findById(petId).getSchedules();

        return setScheduleListToScheduleListDTO(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployee(employeeId);

        if (employee.getSchedules() == null)
            return null;

        List<Schedule> schedules = employee.getSchedules();

        return schedules.stream().map(this::setScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> pets = customerService.findById(customerId).getPets();
        HashMap<Long, Schedule> scheduleHashMap = new HashMap<>();

        pets.stream().forEach(pet -> {
            pet.getSchedules().stream().forEach(thisSchedule -> {
                scheduleHashMap.put(thisSchedule.getId(), thisSchedule);
            });
        });
        return setScheduleListToScheduleListDTO(new ArrayList<>(scheduleHashMap.values()));
    }

    private ScheduleDTO setScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule,scheduleDTO);
        List<Long> employeeId = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> petId = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeId);
        scheduleDTO.setPetIds(petId);

        return scheduleDTO;
    }

    private List<ScheduleDTO> setScheduleListToScheduleListDTO(List<Schedule> schedules) {
        return schedules.stream().map(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
            scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));

            return scheduleDTO;

        }).collect(Collectors.toList());
    }
}
