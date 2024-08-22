package com.sagitDevos.sagitDevos.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagitDevos.sagitDevos.model.constants.Constants;
import com.sagitDevos.sagitDevos.model.dataObjects.EmployeeDataObject;
import com.sagitDevos.sagitDevos.model.dtos.EmployeeDTO;
import com.sagitDevos.sagitDevos.model.dtos.StatusDTO;
import com.sagitDevos.sagitDevos.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UserController implements Constants {

    @Autowired()
    @Qualifier("userService")
    private EmployeeService employeeService;

    @PostMapping("user/create")
    public StatusDTO createUser(EmployeeDataObject dataObject) {
        log.debug("user creation request");
        return employeeService.createEntity(dataObject);
    }

    @PostMapping("users/create")
    public StatusDTO createUsers(@RequestBody String jsonData) {
        log.debug("user creation in bulk request");
        StatusDTO statusDTO = new StatusDTO();
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<EmployeeDataObject> dataObjectList = mapper.readValue(jsonData, mapper.getTypeFactory().constructCollectionType(List.class, EmployeeDataObject.class));
            for (EmployeeDataObject employeeDataObject : dataObjectList){
                employeeService.createEntity(employeeDataObject);
            }
            statusDTO.setErrorCodeAndMessage(SUCCESS, "All Created Successfully");
        } catch (Exception e) {
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    @PostMapping("users/createWithoutMapper")
    public StatusDTO createUsers(@RequestBody List<EmployeeDataObject> dataObjectList) {
        log.debug("user creation in bulk request without mapper");
        StatusDTO statusDTO = new StatusDTO();
        try {
            for (EmployeeDataObject employeeDataObject : dataObjectList){
                employeeService.createEntity(employeeDataObject);
            }
            statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
        } catch (Exception e) {
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    @GetMapping(path = "users/get", produces = {"application/xml", "application/json"})
    public EmployeeDTO getAllUsers(@RequestParam(value = "orderByName", required = false) String orderByName) {
        log.debug("All user fetch request");
        return employeeService.getAllEntities(orderByName);
    }

    @GetMapping("users/by/department")
    public EmployeeDTO getAllUsersByDepartment(@RequestParam("department") String department,
                                               @RequestParam(value = "orderByName", required = false) String orderByName) {
        log.debug("Department user fetch request");
        return employeeService.getAllEntities(department, orderByName);
    }

    @GetMapping("user/id/{id}")
    public EmployeeDataObject getUser(@PathVariable Integer id) {
        return employeeService.getEntityById(id);
    }

    @GetMapping("user/by/Id")
    public EmployeeDataObject getUserById(@RequestParam("id") Integer id) {
        return employeeService.getEntityById(id);
    }

    @DeleteMapping("user/id/{id}")
    public StatusDTO deleteUserById(@PathVariable("id") Integer id) {
        return employeeService.deleteEntityById(id);
    }

    @PutMapping("user/id/{id}")
    public StatusDTO updateUserById(@PathVariable Integer id,
                                    @RequestParam(value = "elseNew", required = false) String ifNotPresentCreateNew,
                                    @RequestBody EmployeeDataObject dataObject) {
        dataObject.setId(id);
        return employeeService.updateEntityById(dataObject, ifNotPresentCreateNew);
    }
}
