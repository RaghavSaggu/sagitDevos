package com.sagitDevos.sagitDevos.service;

import com.sagitDevos.sagitDevos.model.Employee;
import com.sagitDevos.sagitDevos.model.EmployeeDataObject;
import com.sagitDevos.sagitDevos.repository.IEmployeeRepo;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class EmployeeService {
    @Autowired
    IEmployeeRepo employeeRepo;

    public String createEntity(EmployeeDataObject employeeDataObject) {
        try {
            Employee employee = new Employee();
            employee.setEmpName(employeeDataObject.getName());
            employee.setDepartment(employeeDataObject.getDepartment());
            employeeRepo.save(employee);
            return "Created Successfully";
        } catch (ConstraintViolationException exception) {
            exception.printStackTrace();
            return exception.getConstraintViolations().stream().findAny().orElseThrow().getMessage();
        } catch (Exception exception) {
            exception.printStackTrace();
            return "Creation failed";
        }
    }

    public List<EmployeeDataObject> getAllEntities() {
        return this.getAllEntities(null);
    }

    public List<EmployeeDataObject> getAllEntities(String department) {
        List<EmployeeDataObject> employeeDataObjects = new ArrayList<>();
        try {
            List<Employee> employeeList = new ArrayList<>();
            if (StringUtils.isEmpty(department)){
                employeeList = (List<Employee>) employeeRepo.findAll();
            } else {
                employeeList = employeeRepo.findAllByDepartmentContaining(department.toLowerCase());
            }
            for(Employee employee : employeeList) {
                EmployeeDataObject employeeDataObject = new EmployeeDataObject();
                employeeDataObject.setId(employee.getEmpId());
                employeeDataObject.setName(employee.getEmpName());
                employeeDataObject.setDepartment(employee.getDepartment());
                employeeDataObjects.add(employeeDataObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeDataObjects;
    }

    public EmployeeDataObject getEntityById(Integer id) {
        EmployeeDataObject employeeDataObject = new EmployeeDataObject();
        try {
            Employee employee = employeeRepo.findById(id).get();
            employeeDataObject.setId(employee.getEmpId());
            employeeDataObject.setName(employee.getEmpName());
            employeeDataObject.setDepartment(employee.getDepartment());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeDataObject;
    }

    public String deleteEntityById(Integer id) {
        try {
            Employee employee = employeeRepo.findById(id).get();
            employeeRepo.delete(employee);
            return "Deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while deleting";
        }
    }
}
