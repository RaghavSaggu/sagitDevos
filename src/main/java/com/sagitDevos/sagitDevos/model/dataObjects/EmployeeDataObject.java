package com.sagitDevos.sagitDevos.model.dataObjects;

import com.sagitDevos.sagitDevos.model.dtos.StatusDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmployeeDataObject extends StatusDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    Integer id;
    String name;
    String department;
}
