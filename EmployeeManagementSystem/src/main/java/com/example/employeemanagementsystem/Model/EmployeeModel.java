package com.example.employeemanagementsystem.Model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class EmployeeModel {

    @NotEmpty(message = "Your ID is Empty!")
    @Size(min = 3, message = "Length must be more than 2 characters")
    private String ID;

    @NotEmpty(message = "Your Name is Empty!")
    @Size(min = 5, message = "Length must be more than 4 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only characters")
    private String Name;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be 10 digits")
    private String PhoneNumber;

    @NotNull(message = "Age cannot be empty")
    @Positive(message = "Age must be positive")
    @Min(value = 26, message = "Must be more than 25")
    private int age;

    @NotEmpty(message = "Position cannot be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "Position must be 'supervisor' or 'coordinator'")
    private String Position;

    @NotNull
    private boolean onLeave = false;

    @NotNull
    @PastOrPresent
    private LocalDate hireDate;

    @NotNull
    @Positive(message = "Annual Leave must be positive")
    private int AnnualLeave;

}
