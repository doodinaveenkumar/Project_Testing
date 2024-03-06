package com.springboot.repository;


import com.springboot.model.Employee;
//import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
         employee = Employee.builder()
                .firstName("Naveen")
                .lastName("Kumar")
                .email("nk@email.com")
                .build();
    }

    // Junit test for save employee operation
//    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
//        Assertions.assertThat(savedEmployee).isNotNull();
//        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
        // static import
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for get all Employees operation
    @DisplayName("JUnit test for get all Employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        // given - precondition or set up
//        Employee employee1 = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        Employee employee2 = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("jc@email.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindByID_thenReturnEmployeeObject(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    // JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("updatedemail@email.com");
        savedEmployee.setLastName("UpdatedKumar");

        Employee updatedEmployee =  employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("updatedemail@email.com");
        assertThat(updatedEmployee.getLastName()).isEqualTo("UpdatedKumar");
    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
//        employeeRepository.delete(employee);
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    // JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

        String firstName = "Naveen";
        String lastName = "Kumar";

        // when - action or behaviour that we are going to test
        Employee savedEmployee =  employeeRepository.findByJPQL(firstName,lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using JPQL with named params
    @DisplayName("Unit test for custom query using JPQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

        String firstName = "Naveen";
        String lastName = "Kumar";

        // when - action or behaviour that we are going to test
        Employee savedEmployee =  employeeRepository.findByJPQLNamedParams(firstName,lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using native SQl with index
    @DisplayName("JUnit test for custom query using native SQl with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

//        String firstName = "Naveen";
//        String lastName = "Kumar";

        // when - action or behaviour that we are going to test
//        Employee savedEmployee = employeeRepository.findByNativeSQL(firstName,lastName);
        Employee savedEmployee = employeeRepository.findByNativeSQL
                (employee.getFirstName(),employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using native SQl with named params
    @DisplayName("JUnit test for custom query using native SQl with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject(){
        // given - precondition or set up
//        Employee employee = Employee.builder()
//                .firstName("Naveen")
//                .lastName("Kumar")
//                .email("nk@email.com")
//                .build();

        employeeRepository.save(employee);

//        String firstName = "Naveen";
//        String lastName = "Kumar";

        // when - action or behaviour that we are going to test
//        Employee savedEmployee = employeeRepository.findByNativeSQL(firstName,lastName);
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams
                (employee.getFirstName(),employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

}
