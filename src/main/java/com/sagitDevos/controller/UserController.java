package com.sagitDevos.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagitDevos.model.constants.Constants;
import com.sagitDevos.model.dataObjects.EmployeeDataObject;
import com.sagitDevos.model.dtos.EmployeeDTO;
import com.sagitDevos.model.dtos.StatusDTO;
import com.sagitDevos.model.exceptions.InvalidParameterSagitException;
import com.sagitDevos.model.exceptions.UserDefinedSagitException;
import com.sagitDevos.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import com.opencsv.CSVReader;

@Slf4j
@RestController
@RequestMapping("user/")
public class UserController implements Constants {

    @Value(value = "${access.key}")
    private String key;

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

    @PostMapping("s/createUsingCsvOpenCsv")
    public StatusDTO createUsersUsingOpenCsv(MultipartHttpServletRequest request) {
        log.debug("user creation in bulk request using csv");
        StatusDTO statusDTO = new StatusDTO();
        try {
            String headerKey = request.getHeader("accessKey");
            if(!StringUtils.equals(headerKey, key)) {
                log.error("Invalid Credentials");
                statusDTO.setErrorCodeAndMessage(FAIL, "Unauthorised Access");
                return statusDTO;
            }
            MultipartFile file = request.getFile("file");
            List<String[]> data = new ArrayList<>();
            List<EmployeeDataObject> employeeDataObjects = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(file.getInputStream())))) {
                data = reader.readAll();
            }
            for (int i = 0; i<data.size(); i++) {
                if(i == 0) {
                    List<Object> fileHeader = Arrays.asList(data.get(i)[0], data.get(i)[1]);
                    List<Object> expectedHeader = Arrays.asList("name", "department");
                    this.validateHeaders(fileHeader, expectedHeader);
                } else {
                    EmployeeDataObject employeeDataObject = new EmployeeDataObject();
                    employeeDataObject.setName(data.get(i)[0]);
                    employeeDataObject.setDepartment(data.get(i)[1]);
                    employeeDataObjects.add(employeeDataObject);
                }
            }
            if (CollectionUtils.isEmpty(employeeDataObjects))
                throw new UserDefinedSagitException("No user found to create");
            for (EmployeeDataObject employeeDataObject : employeeDataObjects) {
                employeeService.createEntity(employeeDataObject);
            }
            statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
        }  catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    @PostMapping("s/createUsingCsv")
    public StatusDTO createUsersUsingCsv(MultipartHttpServletRequest request) {
        log.debug("user creation in bulk request using csv");
        StatusDTO statusDTO = new StatusDTO();
        try {
            String headerKey = request.getHeader("accessKey");
            if(!StringUtils.equals(headerKey, key)) {
                log.error("Invalid Credentials");
                statusDTO.setErrorCodeAndMessage(FAIL, "Unauthorised Access");
                return statusDTO;
            }
            MultipartFile file = request.getFile("file");
            List<Object> fixedHeader = Arrays.asList("name", "department");
            List<EmployeeDataObject> employeeDataObjects = new ArrayList<>();
            try (InputStream inputStream = file.getInputStream(); BufferedReader csvFile = new BufferedReader(new InputStreamReader(inputStream))) {
                int i = 0;
                Constructor constructor = this.getTheConstructor(EmployeeDataObject.class);
                String line = "";
                while ((line = csvFile.readLine()) != null) {
                    if (i == 0) {
                        this.validateFile(line, fixedHeader);
                    } else {
                        EmployeeDataObject object = (EmployeeDataObject) this.getTheObjectFromLine(EmployeeDataObject.class, line, constructor);
                        employeeDataObjects.add(object);
                    }
                    i++;
                }
            }
            if (CollectionUtils.isEmpty(employeeDataObjects))
                throw new UserDefinedSagitException("No user found to create");
            for (EmployeeDataObject employeeDataObject : employeeDataObjects) {
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

    private <T> Constructor getTheConstructor(Class<T> klass) {
        Constructor constructor = null;
        try {
            Constructor[] constructors = klass.getConstructors();
            if (constructors.length > 1) {
                for (int i = 0; i < constructors.length; i++) {
                    if (constructors[i].getParameterCount() == 1) {
                        constructor = constructors[i];
                    }
                }
            } else {
                constructor = constructors[0];
            }
        } catch (Exception e) {
            throw e;
        }
        return constructor;
    }

    private void validateFile(String line, List<Object> fixedHeadersList) {
        try {
            List<Object> headersList = StringUtils.isNotEmpty(line) ? Arrays.asList(line.split(",")) : null;
            this.validateHeaders(headersList, fixedHeadersList);
        } catch (Exception e) {
            throw e;
        }
    }

    private void validateHeaders(List<Object> headersList, List<Object> fixedHeadersList) {
        try {
            if (!CollectionUtils.isEmpty(headersList) && !CollectionUtils.isEmpty(fixedHeadersList) && headersList.size() != fixedHeadersList.size()) {
                throw new InvalidParameterSagitException("File not allowed: Expected - " + fixedHeadersList.size() + " In File - " + headersList.size());
            } else {
                for (int i = 0; i < fixedHeadersList.size(); i++) {
                    String headerElement = (String) headersList.get(i);
                    String fixedHeaderElement = (String) fixedHeadersList.get(i);
                    if (!headerElement.trim().equals(fixedHeaderElement.trim())) {
                        throw new InvalidParameterSagitException("Header not matched: " + "Expected - " + fixedHeaderElement + " In File - " + headerElement);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private <T> T getTheObjectFromLine(Class<T> klass, String line, Constructor constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        T myObject = null;
        try {
            List<Object> objectList = StringUtils.isNotEmpty(line) ? Arrays.asList(StringUtils.splitPreserveAllTokens(line, ",")) : null;
            if (objectList != null) {
                List<Object> dataList = new ArrayList<>();
                Pattern pattern = Pattern.compile("[a-zA-Z0-9_\\/\\,\\.\\-\\@\\s]*");
                objectList.forEach(object -> {
                    if (!pattern.matcher(object.toString()).matches()) {
                        dataList.add(object.toString().replaceAll("[^a-zA-Z0-9_\\/\\,\\.\\-\\@\\s]*", ""));
                    } else {
                        dataList.add(object);
                    }
                });
                myObject = (T) constructor.newInstance(dataList);
            }
        } catch (Exception e) {
            throw e;
        }
        return myObject;
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
