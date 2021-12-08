package com.relationaldata.organisationdata.department;

import com.relationaldata.organisationdata.employee.Employee;
import com.relationaldata.organisationdata.employee.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping
    List<Department> getDepartment() {
        logger.trace("Fetching department details from db");
        return departmentRepository.findAll();
    }

    @GetMapping("/{did}/employee")
    Set<Employee> getDepartmentEmployee(@PathVariable int did){
        if (did <= 0) {
            logger.warn("Your input is not correct");
        }else {
            logger.info("You can fetch particular department employee by id");
        }

        int fetchDid = did-1;
        return departmentRepository.findAll().get(fetchDid).getEmployees();
    }

    @GetMapping("/{did}/manager")
    Employee getDepartmentManager(@PathVariable int did){
        if (did <= 0) {
            logger.warn("Your input is not correct");
        }else {
            logger.info("You can fetch particular department manager by id");
        }
        int fetchDid = did-1;
        return departmentRepository.findAll().get(fetchDid).getManagerDep();
    }

    @GetMapping("/{did}")
    Optional<Department> getDepartmentById(@PathVariable("did") int did) {
        if (did <= 0) {
            logger.warn("Your input is not correct");
        }else {
            logger.info("You can fetch particular department data by id");
        }

        return departmentRepository.findById(did);
    }

    @PostMapping
    Department createDepartment(@RequestBody Department department) {
        logger.trace("Adding department data");
        return departmentRepository.save(department);
    }

    @PutMapping("/{did}")
    Department assignDepartmentManager(@PathVariable int did,
                                        @RequestBody Employee employ){
        logger.trace("Updating department data with particular manager");
        int eid = employ.getEid();
        Employee employee = employeeRepository.findById(eid).get();
        Department department = departmentRepository.findById(did).get();
        department.assignManager(employee);
        return departmentRepository.save(department);
    }

    @DeleteMapping("/{did}")
    void deleteDepartment(@PathVariable int did){
        logger.info("Deleting department by particular department id");
        for(Employee e: getDepartmentEmployee(did)){
            e.setDepartment(null);
        }
        getDepartmentById(did).get().setManagerDep(null);
        departmentRepository.deleteById(did);
    }
}
