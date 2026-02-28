package com.hr.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hr.entity.Employee;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{
	
public Employee findByIdAndPassword(int empId, String password);


public Employee findByIdAndPasswordAndRole(int empId, String password,String role);

long countByDepartment(String department);

 
// ✅ BIRTHDAY (Native SQL)
@Query(value = "SELECT * FROM employee WHERE MONTH(dob) = MONTH(CURDATE()) AND DAY(dob) >= DAY(CURDATE())", 
       nativeQuery = true)
List<Employee> findUpcomingBirthdays();

// ✅ ANNIVERSARY (Native SQL)
@Query(value = "SELECT * FROM employee WHERE MONTH(joining_date) = MONTH(CURDATE()) AND DAY(joining_date) >= DAY(CURDATE())", 
       nativeQuery = true)
List<Employee> findUpcomingAnniversaries();

List<Employee> findByDepartment(String department); 

List<Employee> findByDateBetween(LocalDate start, LocalDate end);



}
