package com.sagitDevos.repository;

import com.sagitDevos.model.enitities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface IEmployeeJpaRepo extends JpaRepository<Employee, Integer> {
    List<Employee> findAllByDepartment(String department);

    List<Employee> findAllByDepartmentContaining(String department);

    List<Employee> findAllByEmpIdGreaterThanEqual(Integer id);

    @Query("FROM EMPLOYEE ORDER BY empName")
    List<Employee> findAllSortedByName();

    List<Employee> findAllByDepartmentIsOrderByEmpName(String dept);

    List<Employee> findAllByEmpIdIn(List<Integer> empIds);

    List<Employee> findAllByEmpIdInOrderByEmpName(List<Integer> empIds);

}
