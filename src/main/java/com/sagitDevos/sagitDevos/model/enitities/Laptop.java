package com.sagitDevos.sagitDevos.model.enitities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity(name = "LAPTOP")
@ToString
@NoArgsConstructor
public class Laptop {
    @Id
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
