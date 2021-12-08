package com.relationaldata.organisationdata.employee;

import com.fasterxml.jackson.annotation.*;
import com.relationaldata.organisationdata.department.Department;
import com.relationaldata.organisationdata.project.Project;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "eid")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;

    @JsonIgnore
//    @JsonBackReference
    @ManyToMany(mappedBy = "enrolledEmployees")
    private Set<Project> projects = new HashSet<>();

    private String empName;

    @JsonIgnore
//    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "did", referencedColumnName = "did")
    private Department department;

    public void setDepartment(Department department) {
        this.department = department;
    }

//    @JsonBackReference
    @JsonIgnoreProperties(ignoreUnknown = true, value = {"department"})
    @OneToOne(mappedBy = "managerDep")
    Department department1;

    //    @JsonBackReference
    @OneToOne(mappedBy = "managerPro")
    Project project;

    public int getEid() {
        return eid;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Department getDepartment() {
        return department;
    }

//    public Set<Project> getProject() {
//        return projects;
//    }

//    @JsonManagedReference
//    public Department getDepartment1() {
//        return department;
//    }

    public void assignDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "eid=" + eid +
                ", projects=" + projects +
                ", empName='" + empName + '\'' +
                ", department=" + department +
                '}';
    }
}
