package com.hr.service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.entity.CreatePost;
import com.hr.entity.Employee;
import com.hr.repository.CreatePostRepo;
import com.hr.repository.EmployeeRepo;

@Service
public class HrService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private CreatePostRepo createPostRepo;

    // Add employee
    public void addEmployee(Employee employee) {
        employeeRepo.save(employee);
    }

    // Get all employees
    public List<Employee> getAllEmployee() {
        return employeeRepo.findAll();
    }

    // Add post
    public CreatePost addPost(CreatePost createPost) {
        return createPostRepo.save(createPost);
    }

    // Upcoming events for next 7 days
    public List<Employee> getUpcomingEventsFor7Days() {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Employee> events = new ArrayList<>();

        for (Employee emp : employeeRepo.findAll()) {
            // 🎂 Birthday
            if (emp.getDob() != null && !emp.getDob().isEmpty()) {
                LocalDate dobDate = LocalDate.parse(emp.getDob(), formatter);
                LocalDate nextBirthday = getNextEventDate(dobDate, today);
                if (!nextBirthday.isAfter(endDate)) {
                	
                	Employee e = new Employee();
                	e.setEmployeeName(emp.getEmployeeName());
                	e.setEventType("Birthday");
                	e.setDate(nextBirthday);
                	e.setIcon("🎂");
                	events.add(e);
            }
            }

            // 🎉 Anniversary
            if (emp.getJoiningDate() != null && !emp.getJoiningDate().isEmpty()) {
                LocalDate joinDate = LocalDate.parse(emp.getJoiningDate(), formatter);
                LocalDate nextAnniversary = getNextEventDate(joinDate, today);
                if (!nextAnniversary.isAfter(endDate)) {
                	Employee e=new Employee();
                	e.setEmployeeName(emp.getEmployeeName());
                	e.setEventType("Anniversary"); 
                	e.setDate(nextAnniversary); 
                	e.setIcon("🎁");
                	events.add(e); 
                }
            }
        }

        // Sort by upcoming date
        events.sort(Comparator.comparing(Employee::getDate));
        return events;
    }

    // Helper method to calculate next event date
    private LocalDate getNextEventDate(LocalDate originalDate, LocalDate today) {
        MonthDay monthDay = MonthDay.from(originalDate);
        LocalDate eventThisYear = monthDay.atYear(today.getYear());
        if (eventThisYear.isBefore(today)) {
            return monthDay.atYear(today.getYear() + 1);
        }
        return eventThisYear;
    }
}
