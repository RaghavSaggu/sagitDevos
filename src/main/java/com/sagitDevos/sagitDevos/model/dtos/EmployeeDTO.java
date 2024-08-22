package com.sagitDevos.sagitDevos.model.dtos;

import com.sagitDevos.sagitDevos.model.dataObjects.EmployeeDataObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmployeeDTO extends StatusDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    List<EmployeeDataObject> data;
}
