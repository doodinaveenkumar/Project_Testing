package com.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.model.Employee;
import com.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Testcontainers
public class EmployeeControllerIntegrationTestsTestContainer  extends AbstractContainerBaseTest {

    //moved to AbstractContainerBaseTest test class so that can be used in all required classes
//    @Container
//    private static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:latest")
//            .withUsername("username")
//            .withPassword("password")
//            .withDatabaseName("ems");
//
//    @DynamicPropertySource
//    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
//        registry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username",mySQLContainer::getUsername);
//        registry.add("spring.datasource.password",mySQLContainer::getPassword);
//    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    // JUnit test for createEmployee Method
    @DisplayName("JUnit test for createEmployee Method")
    @Test
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

//        System.out.println("MySQL Container Username "+ mySQLContainer.getUsername());
//        System.out.println("MySQL Container Password "+ mySQLContainer.getPassword());
//        System.out.println("MySQL Container Database "+ mySQLContainer.getDatabaseName());
//        System.out.println("MySQL Container URL "+ mySQLContainer.getJdbcUrl());

        // given - precondition or set up
        Employee employee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        // when -- action or behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or the output assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
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

        employeeRepository.saveAll(employeeList);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.size()",
                        is(employeeList.size())));
    }

    // positive scenario - valid employee id
    // JUnit test for getEmployeeById method - positive
    @DisplayName("JUnit test for getEmployeeById method - positive")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
        // given - precondition or set up
        Employee employee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        employeeRepository.save(employee);
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employee.getId()));

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
        Employee employee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",0L));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for updateEmployee method - positive scenario
    @DisplayName("JUnit test for updateEmployee method - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{
        // given - precondition or set up
        Employee savedEmployee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("UpdatedNaveen")
                .lastName("UpdatedKumar")
                .email("updated@email.com")
                .build();

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",savedEmployee.getId())
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
        Employee savedEmployee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("UpdatedNaveen")
                .lastName("UpdatedKumar")
                .email("updated@email.com")
                .build();

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",0L)
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
        Employee savedEmployee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();

        employeeRepository.save(savedEmployee);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }

}
