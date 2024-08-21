package com.sagitDevos.sagitDevos.model;

import org.springframework.stereotype.Component;

@Component("alias")
public class Laptop {
    private Integer lid;
    private String brandName;

    public Laptop() {
        super();
        System.out.println("Unknown Laptop created");
    }

    public int getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "lid=" + lid +
                ", brandName='" + brandName + '\'' +
                '}';
    }

    public void compile(String serverPort) {
        System.out.println("Compiling the code on port " + serverPort);
    }
}
