package com.sagitDevos;

import com.sagitDevos.model.enitities.Employee;
import com.sagitDevos.repository.EmployeeRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootApplication
//@EnableHystrix
public class SagitDevosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SagitDevosApplication.class, args);
//		mainTest();
	}

	public static void mainTest(String[] args) {
		ApplicationContext context = SpringApplication.run(SagitDevosApplication.class, args);
		System.out.println("Started my project");

		Employee employee = context.getBean(Employee.class);
		employee.setEmpId(1);
		employee.setEmpName("Raghav");
		employee.setDepartment("backend");
//		employee.getLaptop().setLid(101);
//		employee.getLaptop().setBrandName("APPLE");
		System.out.println(employee);

		EmployeeRepo repo = context.getBean(EmployeeRepo.class);
		repo.save(employee);
		List<Employee> employeeList = repo.findAll();
		if(!CollectionUtils.isEmpty(employeeList)) {
			for (Employee object : employeeList) {
				System.out.println(object);
			}
		}
	}

}
