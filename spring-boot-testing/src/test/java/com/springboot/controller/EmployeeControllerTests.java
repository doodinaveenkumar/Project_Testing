package com.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.model.Employee;
import com.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // JUnit test for createEmployee Method
    @DisplayName("JUnit test for createEmployee Method")
    @Test
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        // given - precondition or set up
        Employee employee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -- action or behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or the output assert statements
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    // JUnit test for getAllEmployees Method
    @DisplayName("JUnit test for getAllEmployees Method")
    @Test
    public void givenEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception{

        // given - precondition or set up
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(
                Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build());
        employeeList.add(
                Employee.builder()
                .firstName("Tony")
                .lastName("Stark")
                .email("ts@email.com")
                .build());

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeList);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(employeeList.size())));
    }


    // positive scenario - valid employee id
    // JUnit test for getEmployeeById method - positive
    @DisplayName("JUnit test for getEmployeeById method - positive")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
        // given - precondition or set up
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        // then - verify the output
         response.andExpect(status().isOk())
                 .andDo(print())
                 .andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
                 .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                 .andExpect(jsonPath("$.email",is(employee.getEmail())));
    }

    // negative scenario - valid employee id
    // JUnit test for getEmployeeById method - negative
    @DisplayName("JUnit test for getEmployeeById method - negative")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmptyEmployeeObject() throws Exception{
        // given - precondition or set up
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for updateEmployee method - positive scenario
    @DisplayName("JUnit test for updateEmployee method - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{
        // given - precondition or set up
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("UpdatedNaveen")
                .lastName("UpdatedKumar")
                .email("updated@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
        .willAnswer((invocation -> invocation.getArgument(0)));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email",is(updatedEmployee.getEmail())));
    }

    // JUnit test for updateEmployee method - negative scenario
    @DisplayName("JUnit test for updateEmployee method - negative scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
        // given - precondition or set up
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("UpdatedNaveen")
                .lastName("UpdatedKumar")
                .email("updated@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(0)));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
        // given - precondition or set up
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }


}
