package com.springboot.service;

import com.springboot.exception.ResourceNotFoundException;
import com.springboot.model.Employee;
import com.springboot.repository.EmployeeRepository;
import com.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

//    private EmployeeService employeeService;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
         employee = Employee.builder()
                .id(1L)
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given - precondition or set up

//        Employee employee = Employee.builder()
//                .id(1L)
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk2email.com")
//                .build();

//        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
//                .willReturn(Optional.empty());
//
//        BDDMockito.given(employeeRepository.save(employee))
//                .willReturn(employee);

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee))
                .willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        System.out.println(savedEmployee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or set up
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

//        given(employeeRepository.save(employee))
//                .willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,() ->{
            employeeService.saveEmployee(employee);
        });

        // then
       verify(employeeRepository,never()).save(any(Employee.class));
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnListOfEmployees(){
        // given - precondition or set up

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("ts@email.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        // when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        // given - precondition or set up

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("ts@email.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        // given - precondition or set up
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given - precondition or set up
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("updatedemail@email.com");
        employee.setLastName("updatedKumar");

        // when - action or behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("updatedemail@email.com");
        assertThat(updatedEmployee.getLastName()).isEqualTo("updatedKumar");
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        // given - precondition or set up
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);

        // then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
