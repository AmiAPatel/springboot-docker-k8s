package com.sample.employee.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.employee.constants.EmployeeServiceConstants;
import com.sample.employee.model.Employee;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeServiceController {

    List<Employee> employees = new ArrayList<>();
    Employee emp = new Employee();

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";

    }

    @GetMapping("/getemployee")
    public Employee getEmployee() {

        final ObjectMapper jsonMapper = new ObjectMapper();
        try {
            emp = jsonMapper.readValue(new FileReader("/employee.json"), new TypeReference<Employee>() {
            });
            System.out.println("returning employee with id: " + emp.getId());
            return emp;
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
            return emp;
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            return emp;
        } catch (final IOException e) {
            e.printStackTrace();
            return emp;
        }

    }

    @GetMapping("/getallemployees")
    public List<Employee> getAllEmployees() {

        final ObjectMapper jsonMapper = new ObjectMapper();
        try {

            employees = jsonMapper.readValue(new FileReader("/employees.json"), new TypeReference<List<Employee>>() {
            });
            System.out.println("Total Number of employees: " + employees.size());
            return employees;
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
            return employees;
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            return employees;
        } catch (final IOException e) {
            e.printStackTrace();
            return employees;
        }

    }

    @GetMapping("/getemployeebyid")
    public Employee getEmployeeById(@RequestParam final Integer id) {

        final ObjectMapper jsonMapper = new ObjectMapper();
        try {

            employees = jsonMapper.readValue(new FileReader("/employees.json"), new TypeReference<List<Employee>>() {
            });

            for (int index = 0; index < employees.size(); index++) {
                final Employee tempEmp = employees.get(index);
                if (tempEmp.getId() == id) {
                    System.out.println("employee matched with id: " + tempEmp.getId());
                    return tempEmp;
                }
            }
            return emp;
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
            return emp;
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            return emp;
        } catch (final IOException e) {
            e.printStackTrace();
            return emp;
        }

    }

    @PostMapping("/addemployee")
    public String addEmployee(@RequestBody final Employee employee) {

        final JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("/employees.json")) {

            // Read whole JSON file into Array
            Object obj = jsonParser.parse(reader);
            JSONArray employeeArr = (JSONArray) obj;
            System.out.println("employeeArr:" + employeeArr);
            System.out.println("Total Number of employees when request received: " + employeeArr.size());
            //Check if the Object with id already exists 
            Boolean found = false;
            for (int index = 0; index < employeeArr.size(); index++) {
                JSONObject tempEmp = (JSONObject) employeeArr.get(index);
                Long id = (Long)tempEmp.get("id");
                System.out.println("tempEmp.get(id): " + id);
                if (id == employee.getId().longValue()) {
               
                    found = true;
                    System.out.println("Employee already exists with id: " + employee.getId());
                }
            }
            // if object does not exist, add to array and write to file
            if (found == false) {
                JSONObject newJsonObj = new JSONObject();
                newJsonObj.put("id", employee.getId());
                newJsonObj.put("name", employee.getName());
                newJsonObj.put("salary", employee.getSalary());
                newJsonObj.put("maritalStatus", employee.getMaritalStatus());
                employeeArr.add(newJsonObj);
                System.out.println("Added employee with id: " + employee.getId());

                final FileWriter file = new FileWriter("/employees.json");
                file.write(employeeArr.toJSONString());
                file.close();
                System.out.println("Total Number of employees before returning response: " + employeeArr.size());
                return EmployeeServiceConstants.ADD_EMPLOYEE_SUCCESS;
            }else{
                return EmployeeServiceConstants.ADD_EMPLOYEE_FAILURE;    
           }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return EmployeeServiceConstants.ADD_EMPLOYEE_FAILURE;
        }

    }

}