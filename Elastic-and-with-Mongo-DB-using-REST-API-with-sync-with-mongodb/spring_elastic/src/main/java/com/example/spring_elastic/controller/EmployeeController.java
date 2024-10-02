package com.example.spring_elastic.controller;

import com.example.spring_elastic.model.EmployeeMongo;
import com.example.spring_elastic.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // List all employees from MongoDB
    @GetMapping("/list")
    public String viewEmployeesList(Model model) {
        List<EmployeeMongo> employees = employeeService.getAllEmployeesFromMongo();
        model.addAttribute("employees", employees);
        return "employee-list";  // JSP file to display the list of employees
    }

    // Show add employee form
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeMongo());
        return "employee-form";  // JSP form for adding a new employee
    }

    // Save employee (MongoDB and Elasticsearch)
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") EmployeeMongo employee) throws IOException {
        employeeService.saveEmployee(employee);
        return "redirect:/employees/list";  // Redirect to employee list after saving
    }

    // Show edit employee form
    @GetMapping("/edit/{id}")
    public String showUpdateEmployeeForm(@PathVariable String id, Model model) {
        Optional<EmployeeMongo> employee = employeeService.getEmployeeFromMongoDBById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employee-form";  // JSP form for editing an employee
        } else {
            return "redirect:/employees/list";  // Redirect if not found
        }
    }

    // Delete employee by ID (MongoDB and Elasticsearch)
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id) throws IOException {
        employeeService.deleteEmployeeById(id);
        return "redirect:/employees/list";  // Redirect to employee list after deletion
    }
}
