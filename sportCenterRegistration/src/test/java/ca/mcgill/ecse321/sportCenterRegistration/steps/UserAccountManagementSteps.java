package ca.mcgill.ecse321.sportCenterRegistration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.sportCenterRegistration.service.CustomerService;
import ca.mcgill.ecse321.sportCenterRegistration.service.InstructorService;
import ca.mcgill.ecse321.sportCenterRegistration.service.OwnerService;
import ca.mcgill.ecse321.sportCenterRegistration.service.LoginService;
import ca.mcgill.ecse321.sportCenterRegistration.model.Customer;
import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;
import ca.mcgill.ecse321.sportCenterRegistration.model.Owner;
import ca.mcgill.ecse321.sportCenterRegistration.model.Account;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserAccountManagementSteps {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private InstructorService instructorService;
    
    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private CommonSteps commonSteps;
    
    // Context variables to store state between steps
    private String currentPage;
    private String errorMessage;
    private boolean registrationSuccess;
    private boolean loginSuccess;
    private String loggedInUserType;
    private String redirectPage;
    
    // Setup accounts based on the cucumber scenario background
    @Given("the following accounts exist in the system:")
    public void the_following_accounts_exist_in_the_system(DataTable dataTable) {
        List<Map<String, String>> accounts = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> account : accounts) {
            String username = account.get("username");
            String email = account.get("email");
            String password = account.get("password");
            String type = account.get("type");
            
            try {
                switch (type) {
                    case "Customer":
                        if (customerService.getCustomer(username) == null) {
                            customerService.createCustomer(username, email, password);
                        }
                        break;
                    case "Instructor":
                        if (instructorService.getInstructor(username) == null) {
                            instructorService.createInstructor(username, email, password);
                        }
                        break;
                    case "Owner":
                        if (ownerService.getOwner(username) == null) {
                            ownerService.createOwner(username, email, password);
                        }
                        break;
                }
            } catch (Exception e) {
                // Continue if account already exists
                System.out.println("Account already exists: " + e.getMessage());
            }
        }
    }
    
    @Given("I am on the signup page")
    public void i_am_on_the_signup_page() {
        this.currentPage = "signup";
        this.errorMessage = null;
        this.registrationSuccess = false;
        commonSteps.setRegistrationSuccess(false);
    }
    
    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        this.currentPage = "login";
        this.errorMessage = null;
        this.loginSuccess = false;
        this.loggedInUserType = null;
    }
    
    @When("I register with the following details:")
    public void i_register_with_the_following_details(DataTable dataTable) {
        Map<String, String> registration = dataTable.asMaps().get(0);
        String username = registration.get("username");
        String email = registration.get("email");
        String password = registration.get("password");
        
        try {
            Customer customer = customerService.createCustomer(username, email, password);
            if (customer != null) {
                this.registrationSuccess = true;
                commonSteps.setRegistrationSuccess(true);
                this.loggedInUserType = "customer";
                this.redirectPage = "customer-dashboard";
            } else {
                this.registrationSuccess = false;
                commonSteps.setRegistrationSuccess(false);
            }
        } catch (IllegalArgumentException e) {
            this.registrationSuccess = false;
            commonSteps.setRegistrationSuccess(false);
            this.errorMessage = e.getMessage();
            commonSteps.setErrorMessage(e.getMessage());
        }
    }
    
    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        try {
            // First try to log in as a customer
            Customer customer = customerService.customerLogin(username, password);
            if (customer != null) {
                this.loginSuccess = true;
                this.loggedInUserType = "customer";
                this.redirectPage = "customer-dashboard";
                return;
            }
        } catch (IllegalArgumentException e) {
            // Continue to try other login types
        }
        
        try {
            // Then try to log in as an instructor
            Instructor instructor = instructorService.InstructorLogin(username, password);
            if (instructor != null) {
                this.loginSuccess = true;
                this.loggedInUserType = "instructor";
                this.redirectPage = "instructor-dashboard";
                return;
            }
        } catch (IllegalArgumentException e) {
            // Continue to try other login types
        }
        
        try {
            // Finally try to log in as an owner
            Owner owner = ownerService.OwnerLogin(username, password);
            if (owner != null) {
                this.loginSuccess = true;
                this.loggedInUserType = "owner";
                this.redirectPage = "owner-dashboard";
                return;
            }
        } catch (IllegalArgumentException e) {
            // If all login attempts fail, set error message
            this.loginSuccess = false;
            this.errorMessage = "Invalid credentials";
            commonSteps.setErrorMessage("Invalid credentials");
        }
    }
    
    // Removed both duplicate step definitions:
    // 1. the_registration_should_be_successful()
    // 2. the_registration_should_fail_with_error(String expectedError)
    // Now using CommonSteps versions instead
    
    @Then("the login should be successful")
    public void the_login_should_be_successful() {
        assertTrue(this.loginSuccess, "Login should be successful");
    }
    
    @Then("the login should fail with error {string}")
    public void the_login_should_fail_with_error(String expectedError) {
        assertFalse(this.loginSuccess, "Login should fail");
        assertEquals(expectedError, this.errorMessage);
    }
    
    @Then("I should be logged in as a customer")
    public void i_should_be_logged_in_as_a_customer() {
        assertEquals("customer", this.loggedInUserType);
    }
    
    @Then("I should be redirected to the customer dashboard")
    public void i_should_be_redirected_to_the_customer_dashboard() {
        assertEquals("customer-dashboard", this.redirectPage);
    }
    
    @Then("I should be redirected to the instructor dashboard")
    public void i_should_be_redirected_to_the_instructor_dashboard() {
        assertEquals("instructor-dashboard", this.redirectPage);
    }
    
    @Then("I should be redirected to the owner dashboard")
    public void i_should_be_redirected_to_the_owner_dashboard() {
        assertEquals("owner-dashboard", this.redirectPage);
    }
}