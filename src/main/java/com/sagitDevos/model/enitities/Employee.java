package com.sagitDevos.model.enitities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity(name = "EMPLOYEE")
@ToString
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empId;
    @NotEmpty(message = "User name is required")
    private String empName;
    private String department;


//    @Override
//    public String toString() {
//        return "Employee{" +
//                "empId=" + empId +
//                ", empName='" + empName + '\'' +
//                ", department='" + department + '\'' +
//                '}';
//    }
}
