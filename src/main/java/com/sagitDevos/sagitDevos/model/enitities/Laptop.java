package com.sagitDevos.sagitDevos.model.enitities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component("alias")
public class Laptop {
    private Integer lid;
    private String brandName;

//    @Override
//    public String toString() {
//        return "Laptop{" +
//                "lid=" + lid +
//                ", brandName='" + brandName + '\'' +
//                '}';
//    }
}
