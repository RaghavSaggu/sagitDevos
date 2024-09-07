package com.sagitDevos.model.dataObjects;

import com.sagitDevos.model.dtos.StatusDTO;
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
public class EmployeeDataObject extends StatusDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    Integer id;
    String name;
    String department;

    public EmployeeDataObject(List<Object> list) {
        this.name = (String) list.get(0);
        this.department = (String) list.get(1);
//        this.id = Integer.valueOf((String) list.get(2));
    }
}
