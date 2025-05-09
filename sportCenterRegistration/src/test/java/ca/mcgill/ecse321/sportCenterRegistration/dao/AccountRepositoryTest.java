package ca.mcgill.ecse321.sportCenterRegistration.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.sportCenterRegistration.model.Account;
import ca.mcgill.ecse321.sportCenterRegistration.model.Owner;
import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;
import ca.mcgill.ecse321.sportCenterRegistration.model.Customer;

@SpringBootTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private OwnerRepository ownerRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private InstructorRepository instructorRepo;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        accountRepo.deleteAll();
        ownerRepo.deleteAll();
        customerRepo.deleteAll();
        instructorRepo.deleteAll();
    }

    @Test
    // Test creating and reading account As a owner
    public void testCreateAndReadAccount1() {
        // create account
        String username = "Admin";
        String email = "admin@gmail.com";
        String password = "admin123";
        Owner stephenOwner = new Owner(username, email, password);

        // Save in the database
        stephenOwner = ownerRepo.save(stephenOwner);
        int stephenId = stephenOwner.getId();

        // Read from database
        // Staff result = repo.findStaffById(stephenId);
        Account resultAccount = accountRepo.findAccountByUsername(username);

        // check objects
        assertNotNull(resultAccount);

        // check attributes
        assertEquals(stephenId , resultAccount.getId());
        assertEquals(username, resultAccount.getUsername());
        assertEquals(email, resultAccount.getEmail());
        assertEquals(password, resultAccount.getPassword());

        // check reference
        Owner resultOwner = ownerRepo.findOwnerByUsername(username);
        assertNotNull(resultOwner);
        assertEquals(resultAccount.getId(), resultOwner.getId());
    }
    @Test
    // Test creating and reading account As a instructor
    public void testCreateAndReadAccount2() {
        // create account
        String username = "Stephen";
        String email = "admin@sportcenter.com";
        String password = "stephen123";
        Instructor stephenInstructor = new Instructor(username, email, password);

        // Save in the database
        stephenInstructor = instructorRepo.save(stephenInstructor);
        int stephenId = stephenInstructor.getId();

        // Read from database
        // Staff result = repo.findStaffById(stephenId);
        Account resultAccount = accountRepo.findAccountByUsername(username);

        // check objects
        assertNotNull(resultAccount);

        // check attributes
        assertEquals(stephenId , resultAccount.getId());
        assertEquals(username, resultAccount.getUsername());
        assertEquals(email, resultAccount.getEmail());
        assertEquals(password, resultAccount.getPassword());

        // check reference
        Instructor resultInstructor = instructorRepo.findInstructorByUsername(username);
        assertNotNull(resultInstructor);
        assertEquals(resultAccount.getId(), resultInstructor.getId());

    }
    @Test
    // Test creating and reading account As a customer
    public void testCreateAndReadAccount3() {
        // create account
        String username = "Stephen";
        String email = "stephen@gmail.com";
        String password = "stephen123";
        Customer stephenCustomer = new Customer(username, email, password);

        // Save in the database
        stephenCustomer = customerRepo.save(stephenCustomer);
        int stephenId = stephenCustomer.getId();

        // Read from database
        // Staff result = repo.findStaffById(stephenId);
        Account resultAccount = accountRepo.findAccountByUsername(username);

        // check objects
        assertNotNull(resultAccount);

        // check attributes
        assertEquals(stephenId , resultAccount.getId());
        assertEquals(username, resultAccount.getUsername());
        assertEquals(email, resultAccount.getEmail());
        assertEquals(password, resultAccount.getPassword());

        // check reference
        Customer resultCustomer = customerRepo.findCustomerByUsername(username);
        assertNotNull(resultCustomer);
        assertEquals(resultAccount.getId(), resultCustomer.getId());

    }
}