package com.sagitDevos.sagitDevos.repository;

import com.sagitDevos.sagitDevos.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRepo extends CrudRepository<Employee, Integer> {
    List<Employee> findAllByDepartment(String department);

    List<Employee> findAllByDepartmentContaining(String department);

    List<Employee> findAllByEmpIdGreaterThanEqual(Integer id);

    @Query("FROM Employee WHERE empId=?1 ORDER BY empId")
    List<Employee> findAllByEmpIdSorted(Integer id);
}
