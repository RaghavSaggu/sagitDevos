package com.sagitDevos.sagitDevos.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagitDevos.sagitDevos.model.EmployeeDataObject;
import com.sagitDevos.sagitDevos.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired()
    @Qualifier("userService")
    private EmployeeService employeeService;

    @PostMapping("user/add")
    @ResponseBody
    public String createUser(EmployeeDataObject dataObject) {
        System.out.println("user creation request");
        return employeeService.createEntity(dataObject);
    }

    @PostMapping("user/add/bulk")
    @ResponseBody
    public String createUsers(@RequestBody String jsonData) {
        System.out.println("user creation in bulk request");
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<EmployeeDataObject> dataObjectList = mapper.readValue(jsonData, mapper.getTypeFactory().constructCollectionType(List.class, EmployeeDataObject.class));
            for (EmployeeDataObject employeeDataObject : dataObjectList){
                employeeService.createEntity(employeeDataObject);
            }
            return "Created Successfully";
        } catch (Exception e) {
            return "Something went wrong";
        }
    }

    @GetMapping("user/getAll")
    @ResponseBody
    public List<EmployeeDataObject> getAllUsers() {
        System.out.println("user creation request");
        return employeeService.getAllEntities();
    }

    @GetMapping("user/getAllBy/department")
    @ResponseBody
    public List<EmployeeDataObject> getAllUsersByDepartment(@RequestParam("department") String department) {
        System.out.println("user creation request");
        return employeeService.getAllEntities(department);
    }

    @GetMapping("user/get/{id}")
    @ResponseBody
    public EmployeeDataObject getUser(@PathVariable Integer id) {
        return employeeService.getEntityById(id);
    }

    @GetMapping("user/getById")
    @ResponseBody
    public EmployeeDataObject getUserById(@RequestParam("id") Integer id) {
        return employeeService.getEntityById(id);
    }

    @ResponseBody
    @DeleteMapping("user/delete")
    public String deleteUserById(@RequestParam("id") Integer id) {
        return employeeService.deleteEntityById(id);
    }
}
