package com.sagitDevos.sagitDevos.repository;

import com.sagitDevos.sagitDevos.model.enitities.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Slf4j
public class EmployeeRepo {
    private JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }

    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public void save(Employee employee) {
        String insertQuery = "INSERT INTO employee (empid, empname, department) VALUES (?,?,?)";
        int rows = template.update(insertQuery, employee.getEmpId(), employee.getEmpName(), employee.getDepartment());
        log.info("Added rows: " + rows);
    }

    public List<Employee> findAll() {
        String query = "SELECT * FROM employee";
        List<Employee> employeeList = template.query(query, (rs, rowNum) -> {
            Employee a = new Employee();
            a.setEmpId(rs.getInt(1));
            a.setEmpName(rs.getString(2));
            a.setDepartment(rs.getString(3));
            return a;
        });
        return employeeList;
    }
}
