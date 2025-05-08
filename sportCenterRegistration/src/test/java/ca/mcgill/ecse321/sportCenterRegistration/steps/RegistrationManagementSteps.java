package ca.mcgill.ecse321.sportCenterRegistration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.sportCenterRegistration.service.RegistrationService;
import ca.mcgill.ecse321.sportCenterRegistration.service.SessionService;
import ca.mcgill.ecse321.sportCenterRegistration.service.CustomerService;
import ca.mcgill.ecse321.sportCenterRegistration.service.InstructorService;
import ca.mcgill.ecse321.sportCenterRegistration.model.Registration;
import ca.mcgill.ecse321.sportCenterRegistration.model.Session;
import ca.mcgill.ecse321.sportCenterRegistration.model.Customer;
import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;
import ca.mcgill.ecse321.sportCenterRegistration.model.Account;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegistrationManagementSteps {

    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private InstructorService instructorService;
    
    @Autowired
    private CommonSteps commonSteps;
    
    // Context variables to store state between steps
    private boolean operationSuccess;
    private List<Registration> registrationList;
    private Registration currentRegistration;
    private Date currentDate;
    
    @Given("the current date is after {string}")
    public void the_current_date_is_after(String dateStr) {
        // Convert LocalDate to java.sql.Date
        LocalDate localDate = LocalDate.parse(dateStr).plusDays(1);
        this.currentDate = Date.valueOf(localDate);
    }
    
    @Given("I am already registered for session with id {string}")
    public void i_am_already_registered_for_session_with_id(String idStr) {
        int sessionId = Integer.parseInt(idStr);
        
        try {
            // Try to get existing registration
            try {
                this.currentRegistration = registrationService.getRegistrationByAccountAndSession(
                    commonSteps.getCurrentUser(), sessionId);
            } catch (Exception e) {
                // Registration doesn't exist, create it
                Date today = new Date(System.currentTimeMillis());
                this.currentRegistration = registrationService.createRegistration(
                    today, commonSteps.getCurrentUser(), sessionId);
            }
        } catch (Exception e) {
            System.out.println("Error registering for session: " + e.getMessage());
        }
    }
    
    @When("I register for session with id {string}")
    public void i_register_for_session_with_id(String idStr) {
        int sessionId = Integer.parseInt(idStr);
        
        try {
            // Use today's date for registration if current date not specified
            Date registrationDate = (this.currentDate != null) 
                ? this.currentDate 
                : new Date(System.currentTimeMillis());
            
            // Create the registration 
            Registration registration = registrationService.createRegistration(
                registrationDate, commonSteps.getCurrentUser(), sessionId);
            
            this.currentRegistration = registration;
            this.operationSuccess = true;
            commonSteps.setRegistrationSuccess(true);
            
        } catch (Exception e) {
            this.operationSuccess = false;
            commonSteps.setRegistrationSuccess(false);
            commonSteps.setErrorMessage(e.getMessage());
        }
    }
    
    @When("I delete my registration for session with id {string}")
    public void i_delete_my_registration_for_session_with_id(String idStr) {
        int sessionId = Integer.parseInt(idStr);
        
        try {
            // Delete the registration
            Registration deletedRegistration = registrationService.deleteRegistrationByAccountAndSessionId(
                commonSteps.getCurrentUser(), sessionId);
            
            if (deletedRegistration != null) {
                this.operationSuccess = true;
            } else {
                this.operationSuccess = false;
                commonSteps.setErrorMessage("Could not delete registration");
            }
            
        } catch (Exception e) {
            this.operationSuccess = false;
            commonSteps.setErrorMessage(e.getMessage());
        }
    }
    
    // Removed both duplicate step definitions:
    // 1. the_registration_should_be_successful()
    // 2. the_registration_should_fail_with_error(String expectedError)
    // Now using CommonSteps versions instead
    
    @Then("I should see session {string} in my registrations")
    public void i_should_see_session_in_my_registrations(String idStr) {
        int sessionId = Integer.parseInt(idStr);
        
        List<Registration> userRegistrations = registrationService.getRegistrationByAccount(commonSteps.getCurrentUser());
        boolean found = false;
        
        for (Registration r : userRegistrations) {
            if (r.getSession().getId() == sessionId) {
                found = true;
                break;
            }
        }
        
        assertTrue(found, "Session should be found in user's registrations");
    }
    
    @Then("the deletion should be successful")
    public void the_deletion_should_be_successful() {
        assertTrue(this.operationSuccess, "Registration deletion should be successful");
    }
    
    @Then("I should not see session {string} in my registrations")
    public void i_should_not_see_session_in_my_registrations(String idStr) {
        int sessionId = Integer.parseInt(idStr);
        
        List<Registration> userRegistrations = registrationService.getRegistrationByAccount(commonSteps.getCurrentUser());
        boolean found = false;
        
        for (Registration r : userRegistrations) {
            if (r.getSession().getId() == sessionId) {
                found = true;
                break;
            }
        }
        
        assertFalse(found, "Session should not be found in user's registrations");
    }
}