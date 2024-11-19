package com.example.employeemanagementsystem.Controller;


import com.example.employeemanagementsystem.ApiResponse.ApiResponse;
import com.example.employeemanagementsystem.Model.EmployeeModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/ManagementSystem")
public class EmployeeController {

    ArrayList<EmployeeModel> array = new ArrayList<>();


    @GetMapping("/get")
    public ResponseEntity get() {
        return ResponseEntity.ok(array);
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid EmployeeModel model, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        array.add(model);

        return ResponseEntity.status(200).body(new ApiResponse("added"));
    }




    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody @Valid EmployeeModel model, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getID().equals(id)) {
                array.set(i, model);
                return ResponseEntity.status(200).body(new ApiResponse("Updated successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getID().equals(id)) {
                array.remove(i);
                return ResponseEntity.status(200).body(new ApiResponse("Deleted successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
    }



    @GetMapping("/search/{position}")
    public ResponseEntity searchEmployeesByPosition(@PathVariable String position) {


        ArrayList<EmployeeModel> matchedEmployees = new ArrayList<>();


        for (EmployeeModel employee : array) {
            if (employee.getPosition().equals(position)) {
                matchedEmployees.add(employee);
            }
        }

        if (matchedEmployees.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No employees found with the specified positions."));
        }

        return ResponseEntity.status(200).body(new ApiResponse("employees found with the specified positions" + matchedEmployees));
    }


    @GetMapping("/RangeAge/{minAge}/{maxAge}")
    public ResponseEntity<?> getEmployeesByAgeRange(@PathVariable int minAge, @PathVariable int maxAge) {

        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid age range provided!"));
        }

        ArrayList<EmployeeModel> filteredEmployees = new ArrayList<>();

        for (EmployeeModel employee : array) {
            if (employee.getAge() >= minAge && employee.getAge() <= maxAge) {
                filteredEmployees.add(employee);
            }
        }

        if (filteredEmployees.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No employees found in the specified age range."));
        }


        return ResponseEntity.status(200).body(filteredEmployees);
    }



    @PutMapping("/applyLeave/{id}")
    public ResponseEntity applyLeave(@PathVariable String id) {
        for (EmployeeModel employee : array) {
            if (employee.getID().equals(id)) {
                if (employee.isOnLeave()) {
                    return ResponseEntity.status(400).body(new ApiResponse("Employee is already on leave"));
                }

                if (employee.getAnnualLeave() <= 0) {
                    return ResponseEntity.status(400).body(new ApiResponse("No annual leave balance available"));
                }

                employee.setOnLeave(true);
                employee.setAnnualLeave(employee.getAnnualLeave() - 1);

                return ResponseEntity.status(200).body(new ApiResponse("Leave applied successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/noAnnualLeave")
    public ResponseEntity noAnnualLeave() {
        ArrayList<EmployeeModel> employeesWithNoLeave = new ArrayList<>();

        for (EmployeeModel employee : array) {
            if (employee.getAnnualLeave() == 0) {
                employeesWithNoLeave.add(employee);
            }
        }

        if (employeesWithNoLeave.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No employees found with zero annual leave"));
        }

        return ResponseEntity.status(200).body(employeesWithNoLeave);
    }




    @PutMapping("/promote/{id}")
    public ResponseEntity promoteEmployee(@PathVariable String id, @RequestBody String requesterRole) {
        if (!requesterRole.equals("supervisor")) {
            return ResponseEntity.status(400).body(new ApiResponse("Only supervisors can promote employees"));
        }

        for (EmployeeModel employee : array) {
            if (employee.getID().equals(id)) {
                if (employee.getAge() < 30) {
                    return ResponseEntity.status(400).body(new ApiResponse("Employee must be at least 30 years old to be promoted"));
                }

                if (employee.isOnLeave()) {
                    return ResponseEntity.status(400).body(new ApiResponse("Employee cannot be promoted while on leave"));
                }

                employee.setPosition("supervisor");
                return ResponseEntity.status(200).body(new ApiResponse("Employee promoted to supervisor successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
    }



}









