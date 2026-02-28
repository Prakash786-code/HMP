package com.hr.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "employee_name", length=100)
    @Size(min=3,max=100,message=" Employee Name must be 3-100 characters")
    private String employeeName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Gender is required")
    
    private String gender;
    
    //@Pattern(regexp="^\\d{4}-\\d{2}-\\d{2}$")
    @NotBlank(message = "Date of birth is required")
    private String dob;
    
    //@Pattern(regexp="^\\d{4}-\\d{2}-\\d{2}$")
    @NotBlank(message = "Joining date is required")
    private String joiningDate;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", 
             message = "Mobile must start with 6-9 and be 10 digits")
    private String mobile;
    
    @Column(name = "aadhaar_number")
    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar must be 12 digits")
    private String aadhaarNumber;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    
    private String department;

    
    private String designation;

    private String previousCompany;

    @NotBlank(message = "PF number is required")
    private String pfNumber;

    @NotNull(message="Salary is required")
    @Positive(message="Salary must be greater than 0")
    private Double salary;

    @Column(length = 500)
    @NotBlank(message="Current Address is reuired")
    private String currentAddress;

    @Column(length = 500)
    @NotBlank(message="Permanent Address is reuired")
    private String permanentAddress;

    private boolean active = true;
    
    @Column(nullable = false)
    private String password;
    
    @NotBlank(message = "Role is required")
    private String role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedDate;
    
 
      
        
        @Transient
        private String eventType;
        
        @Transient
        private LocalDate date;
        
        @Transient
        private String icon;
        private String name;
        
        public Employee() { 
        }	
        public Employee(String name, String eventType, LocalDate date, String icon) {
            this.name = name;
            this.eventType = eventType;
            this.date = date;
            this.icon = icon;
        }

         // getters/setter

    // ================= GETTERS & SETTERS =================

        
        
public String getEventType() {
    return eventType;
    }

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public LocalDate getDate() {
	return date;
}

public void setDate(LocalDate date) {
	this.date = date;
}

public String getIcon() {
	return icon;
}

public void setIcon(String icon) {
	this.icon = icon;
}

public void setEventType(String eventType) {
    this.eventType = eventType;
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPreviousCompany() {
        return previousCompany;
    }

    public void setPreviousCompany(String previousCompany) {
        this.previousCompany = previousCompany;
    }

    public String getPfNumber() {
        return pfNumber;
    }

    public void setPfNumber(String pfNumber) {
        this.pfNumber = pfNumber;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

   

    public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
    public String toString() {
        return "Employee [employeeName=" + employeeName + ", email=" + email + ", department=" + department + "]";
    }
}
