package com.tacme.employeems.controller;

	import java.util.List;
	 
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.ResponseBody;
	 
	import com.tacme.employeems.service.EmployeeService;
	import com.tacme.employeems.model.Employee;
	  
	/**
	 * Handles requests for the application home page.
	 */
	@Controller
	public class EmployeeController {
	  
	  private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	  public static final String APPLICATION_JSON = "application/json";
	  public static final String APPLICATION_XML = "application/xml";
	  public static final String APPLICATION_HTML = "text/html";
	  
	  @Autowired
	  private EmployeeService employeeService; 
	 
	  
	  /**
	   * Simply selects the home view to render by returning its name.
	  
	   */
	  @RequestMapping(value = "/", method = RequestMethod.GET, produces=APPLICATION_HTML)
	  public @ResponseBody String status() {
	    return "Default Status Message";
	  }
	 
	  @RequestMapping(value="/employees", method=RequestMethod.GET)
	  public @ResponseBody List<Employee> listEmployees() {
	    logger.info("Inside listEmployees() method...");
	  
	    List<Employee> allEmployees = employeeService.listEmployees();
	   
	    return allEmployees;
	  }
	   
	  @RequestMapping(value="/getEmployeeById", method=RequestMethod.GET, produces={APPLICATION_JSON, APPLICATION_XML})
	  public @ResponseBody Employee getEmployeeById(@RequestParam("id") String id) {
		  Employee employee = employeeService.getEmployeeById(Integer.parseInt(id));
	   
	    if (employee != null) {
	      logger.info("Inside getEmployeeById, returned: " + employee.toString());
	    } else {
	      logger.info("Inside getEmployeeById, id: " + id + ", NOT FOUND!");
	    }
	   
	    return employee;
	  }
	   
	 
	  @RequestMapping(value="/employee/delete", method=RequestMethod.DELETE, 
	            produces={APPLICATION_JSON, APPLICATION_XML})
	  public @ResponseBody RestResponse removeEmployee(@RequestParam("id") String id) {
	    RestResponse response;
	  
	    employeeService.removeEmployee(Integer.parseInt(id));
	    logger.info("Inside removeEmployee, deleted: " + id);
	    response = new RestResponse(true, "Successfully deleted Employee: " + id);
	   
	    return response;
	  }
	  
	  @RequestMapping(value="/employee/update", method=RequestMethod.PUT, 
	        consumes={APPLICATION_JSON, APPLICATION_XML}, produces={APPLICATION_JSON, APPLICATION_XML})
	  public @ResponseBody RestResponse updateEmployeeById(@RequestParam("id") String id, 
	                              @RequestBody Employee employee) {
	  RestResponse response;
	  
	  Employee oldEmployee = employeeService.getEmployeeById(Integer.parseInt(id));
	  
	    if (oldEmployee != null) {
	    	oldEmployee.setName(oldEmployee.getName());
	    	oldEmployee.setCountry(oldEmployee.getCountry());
	      logger.info("Inside updateEmployeeById, updated: " + oldEmployee.toString());
	      response = new RestResponse(true, "Successfully updated id: " + oldEmployee.toString());
	    } else {
	      logger.info("Inside updateEmployeeById, id: " + id + ", NOT FOUND!");
	      response = new RestResponse(false, "Failed to update id: " + id);
	    }
	  
	    return response; 
	  }
	  
	  @RequestMapping(value="/employee/add", method=RequestMethod.POST, 
	      consumes={APPLICATION_JSON, APPLICATION_XML}, produces={APPLICATION_JSON, APPLICATION_XML})
	   
	  public @ResponseBody RestResponse addEmployee(@RequestParam("id") String id, @RequestBody Employee employee) {
	    RestResponse response;
	  
	  logger.info("Inside addEmployee, model attribute: " + employee.toString());
	   
	  if (id == null) {
	    response = new RestResponse(false, "id may not be null.");
	    return response;
	  }
	   
	  if (id != null && id.isEmpty()) {
	    response = new RestResponse(false, "id may not be empty.");
	    return response;
	  } 
	 
	  Employee oldEmployee = getEmployeeById(id);
	  if (oldEmployee != null) {
	    if (String.valueOf(oldEmployee.getId()) != null && String.valueOf(oldEmployee.getId()).equalsIgnoreCase(id)) {
	      response = new RestResponse(false, "id already exists in the system.");
	      return response;
	    }else{
	    	logger.info("Inside addEmployee, adding: " + employee.toString());
		      employeeService.addEmployee(employee);
		      response = new RestResponse(true, "Successfully added Employee: " + employee.getId());
	    }
	  }else{
		  response = new RestResponse(true, "Failed to add Employee: " + employee.getId());
	  }
	    return response;
	  }
	}