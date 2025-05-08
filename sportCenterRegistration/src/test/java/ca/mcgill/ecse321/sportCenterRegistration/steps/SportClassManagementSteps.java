package ca.mcgill.ecse321.sportCenterRegistration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.sportCenterRegistration.service.SportClassService;
import ca.mcgill.ecse321.sportCenterRegistration.service.InstructorService;
import ca.mcgill.ecse321.sportCenterRegistration.service.OwnerService;
import ca.mcgill.ecse321.sportCenterRegistration.model.SportClass;
import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;
import ca.mcgill.ecse321.sportCenterRegistration.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SportClassManagementSteps {

    @Autowired
    private SportClassService sportClassService;
    
    @Autowired
    private InstructorService instructorService;
    
    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private CommonSteps commonSteps;
    
    // Context variables to store state between steps
    private String errorMessage;
    private boolean operationSuccess;
    private List<SportClass> sportClassList;
    
    @Given("the following sport classes exist in the system:")
    public void the_following_sport_classes_exist_in_the_system(DataTable dataTable) {
        List<Map<String, String>> sportClasses = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> sportClass : sportClasses) {
            String name = sportClass.get("name");
            boolean approved = Boolean.parseBoolean(sportClass.get("approved"));
            
            try {
                // Try to get the sport class, if it doesn't exist, create it
                try {
                    SportClass existingClass = sportClassService.getSportClass(name);
                    // If approved status is true and current class is not approved, approve it
                    if (approved && !existingClass.getApproved()) {
                        sportClassService.approveSportClass(name);
                    }
                } catch (IllegalArgumentException e) {
                    // Sport class doesn't exist, create it
                    SportClass newClass = sportClassService.createSportClass(name);
                    // If it should be approved, approve it
                    if (approved) {
                        sportClassService.approveSportClass(name);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error creating sport class: " + e.getMessage());
            }
        }
    }
    
    // Removed the duplicate @Given("I am logged in as {string}") method
    // Now using the one from CommonSteps
    
    @Given("a sport class with name {string} exists in the system")
    public void a_sport_class_with_name_exists_in_the_system(String name) {
        try {
            // Check if sport class already exists
            sportClassService.getSportClass(name);
        } catch (IllegalArgumentException e) {
            // Sport class doesn't exist, create it
            sportClassService.createSportClass(name);
        }
    }
    
    @Given("a sport class with name {string} exists in the system with approved status {string}")
    public void a_sport_class_with_name_exists_in_the_system_with_approved_status(String name, String status) {
        boolean approved = Boolean.parseBoolean(status);
        
        try {
            // Check if sport class already exists
            SportClass sportClass = sportClassService.getSportClass(name);
            
            // Set the approved status if necessary
            if (approved && !sportClass.getApproved()) {
                sportClassService.approveSportClass(name);
            }
        } catch (IllegalArgumentException e) {
            // Sport class doesn't exist, create it
            SportClass newClass = sportClassService.createSportClass(name);
            
            // If it should be approved, approve it
            if (approved) {
                sportClassService.approveSportClass(name);
            }
        }
    }
    
    @When("I create a new sport class with name {string}")
    public void i_create_a_new_sport_class_with_name(String name) {
        try {
            if ("instructor".equals(commonSteps.getUserType())) {
                Instructor instructor = instructorService.getInstructor(commonSteps.getCurrentUser());
                sportClassService.createSportClass(name);
                this.operationSuccess = true;
            } else {
                throw new IllegalStateException("Only instructors can create sport classes");
            }
        } catch (Exception e) {
            this.operationSuccess = false;
            this.errorMessage = e.getMessage();
        }
    }
    
    @When("I approve the sport class with name {string}")
    public void i_approve_the_sport_class_with_name(String name) {
        try {
            if ("owner".equals(commonSteps.getUserType())) {
                sportClassService.approveSportClass(name);
                this.operationSuccess = true;
            } else {
                throw new IllegalStateException("Only owners can approve sport classes");
            }
        } catch (Exception e) {
            this.operationSuccess = false;
            this.errorMessage = e.getMessage();
        }
    }
    
    @When("I delete the sport class with name {string}")
    public void i_delete_the_sport_class_with_name(String name) {
        try {
            if ("owner".equals(commonSteps.getUserType())) {
                sportClassService.deleteSportClass(name);
                this.operationSuccess = true;
            } else {
                throw new IllegalStateException("Only owners can delete sport classes");
            }
        } catch (Exception e) {
            this.operationSuccess = false;
            this.errorMessage = e.getMessage();
        }
    }
    
    @When("I request to view all sport classes")
    public void i_request_to_view_all_sport_classes() {
        this.sportClassList = sportClassService.getAllSportClass();
        this.operationSuccess = true;
    }
    
    @Then("the sport class should be created successfully")
    public void the_sport_class_should_be_created_successfully() {
        assertTrue(this.operationSuccess, "Sport class creation should be successful");
    }
    
    @Then("the sport class creation should fail with error {string}")
    public void the_sport_class_creation_should_fail_with_error(String expectedError) {
        assertFalse(this.operationSuccess, "Sport class creation should fail");
        assertEquals(expectedError, this.errorMessage);
    }
    
    @Then("the sport class should appear in the list of sport classes with approved status {string}")
    public void the_sport_class_should_appear_in_the_list_of_sport_classes_with_approved_status(String status) {
        List<SportClass> allClasses = sportClassService.getAllSportClass();
        boolean found = false;
        boolean approvalMatches = false;
        
        for (SportClass sc : allClasses) {
            if (sc.getName().equals(commonSteps.getCurrentUser())) {
                found = true;
                if (Boolean.parseBoolean(status) == sc.getApproved()) {
                    approvalMatches = true;
                }
                break;
            }
        }
        
        assertTrue(found, "Sport class should be found in the list");
        assertTrue(approvalMatches, "Sport class approval status should match expected status");
    }
    
    @Then("the sport class should be approved successfully")
    public void the_sport_class_should_be_approved_successfully() {
        assertTrue(this.operationSuccess, "Sport class approval should be successful");
    }
    
    @Then("the sport class {string} should have approved status {string}")
    public void the_sport_class_should_have_approved_status(String name, String status) {
        SportClass sportClass = sportClassService.getSportClass(name);
        assertNotNull(sportClass, "Sport class should exist");
        assertEquals(Boolean.parseBoolean(status), sportClass.getApproved(), 
                     "Sport class approval status should match expected status");
    }
    
    @Then("the sport class should be deleted successfully")
    public void the_sport_class_should_be_deleted_successfully() {
        assertTrue(this.operationSuccess, "Sport class deletion should be successful");
    }
    
    @Then("the sport class deletion should fail with error {string}")
    public void the_sport_class_deletion_should_fail_with_error(String expectedError) {
        assertFalse(this.operationSuccess, "Sport class deletion should fail");
        assertEquals(expectedError, this.errorMessage);
    }
    
    @Then("the sport class should not appear in the list of sport classes")
    public void the_sport_class_should_not_appear_in_the_list_of_sport_classes() {
        List<SportClass> allClasses = sportClassService.getAllSportClass();
        boolean found = false;
        
        for (SportClass sc : allClasses) {
            if (sc.getName().equals(commonSteps.getCurrentUser())) {
                found = true;
                break;
            }
        }
        
        assertFalse(found, "Sport class should not be found in the list");
    }
    
    @Then("I should see a list of all sport classes")
    public void i_should_see_a_list_of_all_sport_classes() {
        assertNotNull(this.sportClassList, "Sport class list should not be null");
        assertTrue(!this.sportClassList.isEmpty(), "Sport class list should not be empty");
    }
}