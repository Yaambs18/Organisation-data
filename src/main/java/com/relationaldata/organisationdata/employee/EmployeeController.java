package com.relationaldata.organisationdata.employee;

import com.relationaldata.organisationdata.department.Department;
import com.relationaldata.organisationdata.department.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @GetMapping
    List<Employee> getEmployee() {
        logger.trace("Retrieving all employees details");
        return employeeRepository.findAll();
    }

    @GetMapping("/{eid}")
    Optional<Employee> getEmployeeById(@PathVariable("eid") int eid) {
        if (eid <= 0) {
            logger.warn("Your input is not correct");
        }else {
            logger.info("You can fetch particular employee data by id");
        }
        return employeeRepository.findById(eid);
    }

    @PostMapping
    Employee createEmployee(@RequestBody Employee employee) {
        if (employee == null) {
            logger.warn("You are inserting wrong employee details");
        }else {
            logger.trace("Adding employee in database");
        }

        return employeeRepository.save(employee);
    }

    @PutMapping("/{did}")
    Employee assignDepartmentToEmployee(@PathVariable int did,
                                    @RequestBody Employee depart){
        logger.trace("Updating employee data with particular department");
        int eid = depart.getEid();
        Employee employee = employeeRepository.findById(eid).get();
        Department department = departmentRepository.findById(did).get();
        employee.assignDepartment(department);
        return employeeRepository.save(employee);
    }
}
