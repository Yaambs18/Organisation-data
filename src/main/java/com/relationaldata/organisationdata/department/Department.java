package com.relationaldata.organisationdata.department;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
//        property = "did")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int did;

//    @JsonManagedReference
    @OneToMany(mappedBy = "department")
    private Set<Employee> employees = new HashSet<>();

//    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="mid", referencedColumnName = "eid")
    private Employee managerDep;

    public void setManagerDep(Employee managerDep) {
        this.managerDep = managerDep;
    }

    private String depName;

    public int getDid() {
        return did;
    }

    public String getDepName() {
        return depName;
    }

//    @JsonBackReference
    public Employee getManagerDep() {
        return managerDep;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

//    @JsonBackReference
    public Set<Employee> getEmployees() {
        return employees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "did=" + did +
                ", employees=" + employees +
                ", managerDep=" + managerDep +
                ", depName='" + depName + '\'' +
                '}';
    }

    public void assignManager(Employee employee) {
        this.managerDep = employee;
    }
}
