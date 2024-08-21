package com.sagitDevos.sagitDevos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDataObject {
    Integer id;
    String name;
    String department;
}
