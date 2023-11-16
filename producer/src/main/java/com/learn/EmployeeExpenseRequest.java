package com.learn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeExpenseRequest {
    private String employeeId;
    private String employeeName;
    private String designation;
    private String joiningDate;
    private List<Error> errors;
}
