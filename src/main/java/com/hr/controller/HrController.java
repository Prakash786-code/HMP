package com.hr.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.entity.Compose;
import com.hr.entity.CreatePost;
import com.hr.entity.Employee;
import com.hr.repository.ComposeRepo;
import com.hr.repository.CreatePostRepo;
import com.hr.repository.EmployeeRepo;
import com.hr.service.HrService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HrController {
	
	@Autowired
	private HrService hrService;
	

	@Autowired
	private CreatePostRepo createPostRepo;
	
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private ComposeRepo composeRepo;

	
   @GetMapping("/login")
  
	public String login() {
		return "login";
	}
   
	
   
   @GetMapping("/logout")
   public String logout(HttpSession session) {

       session.invalidate();   // destroy session

       return "redirect:/login";
   }

   
   
	
   @GetMapping("/forgot-password")
  
	public String forgotPassword() {
		return "forgot-password";
	}
   //Add new 
   @PostMapping("/login")
   public String loginProcess(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model,
                              HttpSession session) {

       System.out.println("Login Attempt: " + username);

       // Check username format
       if (!username.startsWith("HMP") || username.length() <= 3) {
           model.addAttribute("error", true);
           return "login";
       }

       try {
           int empId = Integer.parseInt(username.substring(3));

           Employee employee = employeeRepo
                   .findByIdAndPassword(empId, password);

           if (employee != null) {

               session.setAttribute("userId", employee.getId());
               session.setAttribute("name", employee.getEmployeeName());
               session.setAttribute("desg", employee.getDesignation());

               if ("USER".equalsIgnoreCase(employee.getRole())) {
                   return "redirect:/user-dashboard";
               } else if ("ADMIN".equalsIgnoreCase(employee.getRole())) {
                   return "redirect:/dash-board";
               }
           }

       } catch (Exception e) {
           e.printStackTrace();
       }

       model.addAttribute("error", true);
       return "login";
   }
   
   @GetMapping("/dash-board")
   public String dashBoard(Model model, HttpSession session) {

       // ===== 🔹 ADD THIS FOR DROPDOWN =====
       Integer userId = (Integer) session.getAttribute("userId");

       if (userId != null) {
           Employee employee = employeeRepo.findById(userId).orElse(null);
           model.addAttribute("employee", employee);
       }
       // ====================================

       long total = composeRepo.count();
       long pending = composeRepo.countByStatus("pending");
       long approved = composeRepo.countByStatus("approved");
       long denied = composeRepo.countByStatus("denied");

       long development = employeeRepo.countByDepartment("Development");
       long qa = employeeRepo.countByDepartment("QA & Automation Testing");
       long networking = employeeRepo.countByDepartment("Networking");
       long hr = employeeRepo.countByDepartment("HR Team");
       long security = employeeRepo.countByDepartment("Security");
       long sales = employeeRepo.countByDepartment("Sales Marketing");

       List<Compose> recentList = composeRepo.findTop5ByOrderByCreatedDateDesc();
       model.addAttribute("recentList", recentList);

       List<Employee> events = hrService.getUpcomingEventsFor7Days();
       model.addAttribute("events", events);

       model.addAttribute("total", total);
       model.addAttribute("pending", pending);
       model.addAttribute("approved", approved);
       model.addAttribute("denied", denied);

       model.addAttribute("development", development);
       model.addAttribute("qa", qa);
       model.addAttribute("networking", networking);
       model.addAttribute("hr", hr);
       model.addAttribute("security", security);
       model.addAttribute("sales", sales);

       return "dash-board";
   }
   
   @GetMapping("/add-employee")
   public String addEmployeePage(Model model) {
       model.addAttribute("employee", new Employee());
       return "add-employee";
   }

   @GetMapping("/all-employee")
   public String allEmployee(Model model) {

       List<Employee> allEmployee = hrService.getAllEmployee();

       System.out.println("Employees size: " + allEmployee.size());

       model.addAttribute("allEmployee", allEmployee);

       return "all-employee";
   }

   
   
   @GetMapping("/create-post")
   public String createPost (Model model) {
	   
	  List<CreatePost> findAll= createPostRepo.findAll();
	  model.addAttribute("post",findAll);
	   
	   return "create-post";
   }
   
   @GetMapping("/status")
   public String Status(Model model) {
	   
	  List<Compose>findAll= composeRepo.findAll();
	  
	  //java 8
	  findAll.stream()
	  .forEach(k->{
		  int id=k.getParentUkid();
		  String designation = employeeRepo.findById(id)
	                .map(emp -> emp.getDesignation())
	                .orElse("N/A");
		  k.setPosition(designation);
	  });
	  
		model.addAttribute("statusList", findAll) ; 
	   
	   return "status";
   }
   @GetMapping("/my-profile")
   public String MyProfile(HttpSession session,Model model) {
	   
	 Object attribute=  session.getAttribute("userId");
	 
	int userId= Integer.parseInt(attribute.toString());
	
	Employee employee =employeeRepo.findById(userId).get();	 
	model.addAttribute("employee", employee);
	
	 System.out.println("Object:-"+attribute);
	   
	   return "my-profile";
   }
   @GetMapping("/setting")
   public String Setting() {
	   
	   return "setting";
   }
   
  
   @PostMapping("/save-employee")
   public String saveEmployee(
           @Valid @ModelAttribute("employee") Employee employee,
           BindingResult result,
           Model model) {
if(result.hasErrors()) {
	model.addAttribute("employee",employee);
	return"add-employee";
}
       employee.setPassword(employee.getDob());

       hrService.addEmployee(employee);

       return "redirect:/dash-board";
   }
   

   
   @PostMapping("/save-post")
   public String savePost(@ModelAttribute CreatePost createPost) {
	   
	   
	   createPost.setAddedDate(new Date().toString());
       CreatePost addPost =hrService.addPost(createPost);

       return "redirect:/create-post";
       
   }
   @PostMapping("/update-password")
   public String updatePassword( 
		   @RequestParam("password")String password, 
		    @RequestParam("newPassword1")String newPassword1,
		    @RequestParam("newPassword2")String newPassword2,
		    HttpSession session ,Model model) {
	   
	   System.out.println(password+" - "+newPassword1+"-"+newPassword2);
	   
	   Object attribute=  session.getAttribute("userId");
		 
		int userId= Integer.parseInt(attribute.toString());
		
		Employee employee =employeeRepo.findByIdAndPassword(userId,password);	
	   
	   if ( employee !=null && newPassword1.equals(newPassword2)) {
		    employee.setPassword(newPassword2);
		    employeeRepo.save(employee);
		   
	   }else {
		   model.addAttribute("error", false);
		   
		   return "setting";
		   
		   
	   }
	   
	   return "redirect:/login";
   }   
   @PostMapping("/update-profile")
   public String updateProfile(@ModelAttribute Employee formEmployee,
                               HttpSession session) {

       Object attribute = session.getAttribute("userId");

       if (attribute == null) {
           return "redirect:/login";
       }

       int userId = Integer.parseInt(attribute.toString());

       Employee existingEmployee = employeeRepo.findById(userId).orElse(null);

       if (existingEmployee == null) {
           return "redirect:/login";
       }

       // Update fields
       existingEmployee.setEmployeeName(formEmployee.getEmployeeName());
       existingEmployee.setEmail(formEmployee.getEmail());
       existingEmployee.setMobile(formEmployee.getMobile());
       existingEmployee.setDepartment(formEmployee.getDepartment());
       existingEmployee.setDesignation(formEmployee.getDesignation());
       existingEmployee.setCurrentAddress(formEmployee.getCurrentAddress());
       existingEmployee.setPermanentAddress(formEmployee.getPermanentAddress());

       employeeRepo.save(existingEmployee);

       return "redirect:/my-profile";  // 🔥 IMPORTANT
   }
   @GetMapping("/edit-employee/{id}")
   public String editEmployee(@PathVariable Integer id, Model model) {

       Employee employee = employeeRepo.findById(id).orElse(null);

       model.addAttribute("employee", employee);

       return "edit-employee";
   }
   @PostMapping("/update-employee")
   public String updateEmployee(@ModelAttribute Employee formEmployee) {

       Employee existing = employeeRepo
               .findById(formEmployee.getId())
               .orElse(null);
       if (existing != null) {

           existing.setEmployeeName(formEmployee.getEmployeeName());
           existing.setEmail(formEmployee.getEmail());
           existing.setGender(formEmployee.getGender());
           existing.setDob(formEmployee.getDob());
           existing.setJoiningDate(formEmployee.getJoiningDate());
           existing.setMobile(formEmployee.getMobile());
           existing.setDepartment(formEmployee.getDepartment());
           existing.setDesignation(formEmployee.getDesignation());
           existing.setAadhaarNumber(formEmployee.getAadhaarNumber());   // 🔥 ADD THIS
           existing.setAccountNumber(formEmployee.getAccountNumber());   // 🔥 ADD THIS
           existing.setPfNumber(formEmployee.getPfNumber());             // 🔥 ADD THIS
           existing.setPreviousCompany(formEmployee.getPreviousCompany());
           existing.setCurrentAddress(formEmployee.getCurrentAddress());
           existing.setPermanentAddress(formEmployee.getPermanentAddress());
           existing.setSalary(formEmployee.getSalary());
           existing.setActive(formEmployee.isActive());

           employeeRepo.save(existing);
       }

       return "redirect:/all-employee";
   }
      
   @GetMapping("/delete-employee/{id}")
   public String deleteEmployee(@PathVariable Integer id) {

       employeeRepo.deleteById(id);

       return "redirect:/all-employee";
   }
   
   @GetMapping("/user-dashboard")
   public String userDashboard(
           @RequestParam(value = "status", required = false) String status,
           Model model,
           HttpSession session) {

       Integer userId = (Integer) session.getAttribute("userId");

       // ✅ ADD THIS EXACTLY HERE
       Employee employee = employeeRepo.findById(userId).orElse(null);
       model.addAttribute("employee", employee);

       long pending = composeRepo.countByParentUkidAndStatus(userId, "pending");
       long approved = composeRepo.countByParentUkidAndStatus(userId, "approved");
       long denied = composeRepo.countByParentUkidAndStatus(userId, "denied");
       long total = composeRepo.countByParentUkid(userId);

       model.addAttribute("pending", pending);
       model.addAttribute("approved", approved);
       model.addAttribute("denied", denied);
       model.addAttribute("total", total);

       List<Compose> list;

       if (status != null) {
           list = composeRepo.findByParentUkidAndStatus(userId, status);
           model.addAttribute("currentStatus", status);
       } else {
           list = composeRepo.findByParentUkid(userId);
           model.addAttribute("currentStatus", "all");
       }

       model.addAttribute("statusList", list);

       // birthday events (already added before)
       List<Employee> events = hrService.getUpcomingEventsFor7Days();
       model.addAttribute("events", events);

       return "user-dash-board";
   }
   
   
   @GetMapping("/user-profile")
   public String userprofile(HttpSession session,Model model) {
	   
	 Object attribute=  session.getAttribute("userId");
	 
	int userId= Integer.parseInt(attribute.toString());
	
	Employee employee =employeeRepo.findById(userId).get();	 
	model.addAttribute("employee", employee);
	
	 System.out.println("Object:-"+attribute);
	   
	   return "user-profile";
   }
   @GetMapping("/user-setting")
   public String userSetting() {
	   
	   return "user-setting";
   }
   
   @GetMapping("/user-compose")
   public String userCompose(Model model) {

       List<Compose> list = composeRepo.findAll();
       model.addAttribute("composeList", list);

       return "user-compose";
   } 
   
   @PostMapping("/compose")
   public String addCompose(@RequestParam()String subject,@RequestParam("text") String text ,HttpSession session ) {
	  
	   try {
		     Object attribute=  session.getAttribute("userId");
			 
		 		int userId= Integer.parseInt(attribute.toString());
		 		
		 		Employee employee =employeeRepo.findById(userId).get();
		 		
		 		Compose com=new Compose();
		 		com.setEmpName(employee.getEmployeeName());
		 		com.setSubject(subject);
		 		com.setText(text);
		 		com.setParentUkid(userId);
		 		com.setAddedDate(new Date().toString());
		 		com.setStatus("pending");
		 		
		 		
		 		Compose save=composeRepo.save(com);
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 		
	   return "redirect:/user-compose";
	   
   }
   
   @GetMapping("/update-status/{id}/{status}")
   public String updateStatus(@PathVariable Integer id,
                              @PathVariable String status) {

       Compose compose = composeRepo.findById(id).orElse(null);

       if (compose != null) {
           compose.setStatus(status);
           composeRepo.save(compose);
       }

       return "redirect:/status";
   }
   
   @GetMapping("/requests/{status}")
   public String getRequestsByStatus(@PathVariable String status, Model model) {
       List<Compose> list;
       if (status.equals("all")) {
           list = composeRepo.findAll();
       } else {
           list = composeRepo.findAll().stream()
                   .filter(c -> c.getStatus().equalsIgnoreCase(status))
                   .toList();
       }
       model.addAttribute("statusList", list);
       model.addAttribute("filter", status);
       return "status";  // reuse your status.html page
   }
   
   @GetMapping("/employee/list")
   public String getEmployeesByDepartment(@RequestParam("department") String department, Model model) {
       // Fetch employees belonging to the selected department
       List<Employee> employees = employeeRepo.findByDepartment(department);

       // Add filtered employees to the model
       model.addAttribute("allEmployee", employees);
       model.addAttribute("selectedDepartment", department);

       // Reuse the same all-employee view
       return "all-employee";
   }
   

}

