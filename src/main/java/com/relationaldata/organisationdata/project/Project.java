package com.relationaldata.organisationdata.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.relationaldata.organisationdata.employee.Employee;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "pid")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    @ManyToMany
//    @JsonManagedReference
    @JoinTable(
            name = "employee_enrolled",
            joinColumns = @JoinColumn(name = "pid"),
            inverseJoinColumns = @JoinColumn(name = "eid"))
    private Set<Employee> enrolledEmployees = new HashSet<>();

//    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="mid", referencedColumnName = "eid")
    private Employee managerPro;

    private String proName;

    public int getPid() {
        return pid;
    }

    public String getProName() {
        return proName;
    }

    public Employee getManagerPro() {
        return managerPro;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Set<Employee> getEnrolledEmployees() {
        return enrolledEmployees;
    }

    public void enrollEmployee(Employee employee) {
        enrolledEmployees.add(employee);
    }

    @Override
    public String toString() {
        return "Project{" +
                "pid=" + pid +
                ", enrolledEmployees=" + enrolledEmployees +
                ", managerPro=" + managerPro +
                ", proName='" + proName + '\'' +
                '}';
    }

    public void assignManager(Employee employee) {
        this.managerPro = employee;
    }
}
