package com.relationaldata.organisationdata.project;

import com.relationaldata.organisationdata.employee.Employee;
import com.relationaldata.organisationdata.employee.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/project")
public class ProjectController {

    Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping
    List<Project> getProject() {
        logger.trace("Finding projects from db");
        return projectRepository.findAll();
    }

    @GetMapping("/{pid}")
    Optional<Project> getProjectById(@PathVariable("pid") int pid) {
        if (pid <= 0) {
            logger.warn("Your input is not correct");
        }else {
            logger.info("You can fetch particular project data by id");
        }

        return projectRepository.findById(pid);
    }

    @GetMapping("/{pid}/employee")
    Set<Employee> getProjectEmployee(@PathVariable int pid){
        if (pid <= 0) {
            logger.warn("Your input is not correct");
        }else {
            logger.info("You can fetch particular project employee by id");
        }

        int fetchPid = pid-1;
        return projectRepository.findAll().get(fetchPid).getEnrolledEmployees();
    }

    @GetMapping("/{pid}/manager")
    Employee getProjectManager(@PathVariable int pid){
        if (pid <= 0) {
            logger.warn("Your input is not correct");
        }else {
            logger.info("You can fetch particular project manager by id");
        }

        int fetchPid = pid-1;
        return projectRepository.findAll().get(fetchPid).getManagerPro();
    }

    @PostMapping
    Project createProject(@RequestBody Project project) {
        logger.trace("Adding project details");
        return projectRepository.save(project);
    }

    @PutMapping("/{pid}/employee")
    Project enrollEmployeeToProject(@PathVariable int pid,
                                    @RequestBody Employee employ){
        logger.trace("Updating project with employee details");
        int eid = employ.getEid();
        Project project = projectRepository.findById(pid).get();
        Employee employee = employeeRepository.findById(eid).get();
        project.enrollEmployee(employee);
        return projectRepository.save(project);
    }

    @PutMapping("/{mid}")
    Project assignProjectManager(@PathVariable int mid,
                                       @RequestBody Employee employ){
        logger.trace("Updating project data with particular manager");
        int eid = employ.getEid();
        Employee employee = employeeRepository.findById(eid).get();
        Project project = projectRepository.findById(mid).get();
        project.assignManager(employee);
        return projectRepository.save(project);
    }
}
