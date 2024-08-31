package com.sagitDevos.sagitDevos.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagitDevos.sagitDevos.model.constants.Constants;
import com.sagitDevos.sagitDevos.model.dataObjects.EmployeeDataObject;
import com.sagitDevos.sagitDevos.model.dtos.EmployeeDTO;
import com.sagitDevos.sagitDevos.model.dtos.StatusDTO;
import com.sagitDevos.sagitDevos.model.exceptions.UserDefinedSagitException;
import com.sagitDevos.sagitDevos.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("user/")
public class UserController implements Constants {

    @Autowired()
    @Qualifier("userService")
    private EmployeeService employeeService;

    @PostMapping("create")
    public StatusDTO createUser(EmployeeDataObject dataObject) {
        log.debug("user creation request");
        return employeeService.createEntity(dataObject);
    }

    @GetMapping("by/id")
    public EmployeeDataObject getUserById(@RequestParam("id") Integer id) {
        return employeeService.getEntityById(id);
    }

    @GetMapping("id/{id}")
    public EmployeeDataObject getUser(@PathVariable Integer id) {
        return employeeService.getEntityById(id);
    }

    @DeleteMapping("id/{id}")
    public StatusDTO deleteUserById(@PathVariable("id") Integer id) {
        return employeeService.deleteEntityById(id);
    }

    @PutMapping("id/{id}")
    public StatusDTO updateUserById(@PathVariable Integer id,
                                    @RequestParam(value = "elseNew", required = false) String ifNotPresentCreateNew,
                                    @RequestBody EmployeeDataObject dataObject) {
        dataObject.setId(id);
        return employeeService.updateEntityById(dataObject, ifNotPresentCreateNew);
    }

    @PostMapping("s/create")
    public StatusDTO createUsers(@RequestBody String jsonData) {
        log.debug("user creation in bulk request");
        StatusDTO statusDTO = new StatusDTO();
        try {
            if(StringUtils.isEmpty(jsonData))
                throw new UserDefinedSagitException("No user found");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<EmployeeDataObject> dataObjectList = mapper.readValue(jsonData, mapper.getTypeFactory().constructCollectionType(List.class, EmployeeDataObject.class));
            for (EmployeeDataObject employeeDataObject : dataObjectList){
                employeeService.createEntity(employeeDataObject);
            }
            statusDTO.setErrorCodeAndMessage(SUCCESS, "All Created Successfully");
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    @PostMapping("s/createWithoutMapper")
    public StatusDTO createUsers(@RequestBody List<EmployeeDataObject> dataObjectList) {
        log.debug("user creation in bulk request without mapper");
        StatusDTO statusDTO = new StatusDTO();
        try {
            if(CollectionUtils.isEmpty(dataObjectList))
                throw new UserDefinedSagitException("No user found");
            for (EmployeeDataObject employeeDataObject : dataObjectList){
                employeeService.createEntity(employeeDataObject);
            }
            statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    @GetMapping(path = "s/get", produces = {"application/xml", "application/json"})
    public EmployeeDTO getAllUsers(@RequestParam(value = "orderByName", required = false) String orderByName) {
        log.debug("All user fetch request");
        EmployeeDTO employeeDTO = new EmployeeDTO();
        try {
            employeeDTO = employeeService.getAllEntitiesByDepartment(orderByName);
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            employeeDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return employeeDTO;
    }

    @GetMapping("s/by/department")
    public EmployeeDTO getAllUsersByDepartment(@RequestParam("department") String department,
                                               @RequestParam(value = "orderByName", required = false) String orderByName) {
        log.debug("Department user fetch request");
        EmployeeDTO employeeDTO = new EmployeeDTO();
        try {
            employeeDTO = employeeService.getAllEntitiesByDepartment(department, orderByName);
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            employeeDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return employeeDTO;
    }

    @GetMapping("s/by/ids")
    public EmployeeDTO getUserListByIds(@RequestParam("ids") String idList,
                                        @RequestParam(value = "orderByName", required = false) String orderByName) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        try {
            employeeDTO = employeeService.getAllEntitiesByIds(idList, orderByName);
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            employeeDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return employeeDTO;
    }
}
