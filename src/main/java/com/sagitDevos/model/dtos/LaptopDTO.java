package com.sagitDevos.model.dtos;

import com.sagitDevos.model.dataObjects.LaptopDataObject;
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
public class LaptopDTO extends StatusDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    List<LaptopDataObject> data;
}
