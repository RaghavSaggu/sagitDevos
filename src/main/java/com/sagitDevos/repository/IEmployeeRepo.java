package com.sagitDevos.repository;

import com.sagitDevos.model.enitities.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRepo extends CrudRepository<Employee, Integer> {
    List<Employee> findAllByDepartment(String department);

    List<Employee> findAllByDepartmentContaining(String department);

    List<Employee> findAllByEmpIdGreaterThanEqual(Integer id);

    @Query("FROM EMPLOYEE WHERE department=?1 ORDER BY empId")
    List<Employee> findAllByDepartmentSorted(String department);
}
