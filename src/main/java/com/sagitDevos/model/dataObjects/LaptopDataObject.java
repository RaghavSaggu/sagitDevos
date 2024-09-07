package com.sagitDevos.model.dataObjects;

import com.sagitDevos.model.dtos.StatusDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LaptopDataObject extends StatusDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer lid;
    private String brandName;
}
