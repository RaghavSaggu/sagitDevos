package com.sagitDevos.sagitDevos.service;

import com.sagitDevos.sagitDevos.model.constants.Constants;
import com.sagitDevos.sagitDevos.model.dtos.EmployeeDTO;
import com.sagitDevos.sagitDevos.model.dtos.StatusDTO;
import com.sagitDevos.sagitDevos.model.enitities.Employee;
import com.sagitDevos.sagitDevos.model.dataObjects.EmployeeDataObject;
import com.sagitDevos.sagitDevos.repository.IEmployeeJpaRepo;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class EmployeeService implements Constants {
    @Autowired
    IEmployeeJpaRepo employeeRepo;

    public StatusDTO createEntity(EmployeeDataObject employeeDataObject) {
        StatusDTO statusDTO= new StatusDTO();
        try {
            Employee employee = new Employee();
            employee.setEmpName(employeeDataObject.getName());
            employee.setDepartment(employeeDataObject.getDepartment());
            employeeRepo.save(employee);
            statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
        } catch (ConstraintViolationException exception) {
            exception.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, exception.getConstraintViolations().stream().findAny().orElseThrow().getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    public EmployeeDTO getAllEntities(String orderByName) {
        return this.getAllEntities(null, orderByName);
    }

    public EmployeeDTO getAllEntities(String department, String orderByName) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        try {
            List<Employee> employeeList;
            if (StringUtils.isEmpty(department)){
                employeeList = org.thymeleaf.util.StringUtils.equals(orderByName, "Y") ?
                        employeeRepo.findAllSortedByName() :
                        employeeRepo.findAll();
            } else {
                employeeList = org.thymeleaf.util.StringUtils.equals(orderByName, "Y") ?
                        employeeRepo.findAllByDepartmentIsOrderByEmpName(department) :
                        employeeRepo.findAllByDepartment(department);
            }
            if(CollectionUtils.isEmpty(employeeList)) {
                employeeDTO.setErrorCodeAndMessage(FAIL, NO_RECORD);
            } else {
                List<EmployeeDataObject> employeeDataObjects = new ArrayList<>();
                for (Employee employee : employeeList) {
                    EmployeeDataObject employeeDataObject = new EmployeeDataObject();
                    employeeDataObject.setId(employee.getEmpId());
                    employeeDataObject.setName(employee.getEmpName());
                    employeeDataObject.setDepartment(employee.getDepartment());
                    employeeDataObjects.add(employeeDataObject);
                }
                employeeDTO.setData(employeeDataObjects);
                employeeDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeDTO;
    }

    public EmployeeDataObject getEntityById(Integer id) {
        EmployeeDataObject employeeDataObject = new EmployeeDataObject();
        try {
            Employee employee = employeeRepo.findById(id).orElse(null);
            if(employee != null){
                employeeDataObject.setId(employee.getEmpId());
                employeeDataObject.setName(employee.getEmpName());
                employeeDataObject.setDepartment(employee.getDepartment());

                employeeDataObject.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            } else {
                employeeDataObject.setErrorCodeAndMessage(FAIL, NO_RECORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            employeeDataObject.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return employeeDataObject;
    }

    public StatusDTO deleteEntityById(Integer id) {
        StatusDTO statusDTO = new StatusDTO();
        try {
            Employee employee = employeeRepo.findById(id).orElse(null);
            if(employee != null) {
                employeeRepo.delete(employee);
                statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            } else {
                statusDTO.setErrorCodeAndMessage(FAIL, NO_RECORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    public StatusDTO updateEntityById(EmployeeDataObject dataObject, String ifNotPresentCreateNew) {
        StatusDTO statusDTO = new StatusDTO();
        try {
            Employee employee = employeeRepo.findById(dataObject.getId()).orElse(null);
            if (employee == null) {
                statusDTO.setErrorCodeAndMessage(FAIL, NO_RECORD);
                if(org.apache.commons.lang3.StringUtils.equals(ifNotPresentCreateNew, STRING_Y)) {
                    statusDTO = this.createEntity(dataObject);
                }
            } else {
                employee.setEmpName(dataObject.getName());
                employee.setDepartment(dataObject.getDepartment());
                employeeRepo.save(employee);
                statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }
}
