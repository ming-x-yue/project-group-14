package ca.mcgill.ecse321.sportCenterRegistration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

// Removed @Component annotation as it causes conflicts with Cucumber's own registration
public class CommonSteps {
    
    // Shared variables to be accessed by other step definition classes
    private String currentUser;
    private String userType;
    private boolean registrationSuccess;
    private String errorMessage;
    
    @Given("I am logged in as {string}")
    public void i_am_logged_in_as(String username) {
        this.currentUser = username;
        
        // Determine user type based on username
        if (username.startsWith("customer")) {
            this.userType = "customer";
        } else if (username.startsWith("instruc")) {
            this.userType = "instructor";
        } else if (username.startsWith("owner")) {
            this.userType = "owner";
        } else {
            throw new IllegalArgumentException("Unknown user type for username: " + username);
        }
    }
    
    // Common step for checking if registration was successful
    @Then("the registration should be successful")
    public void the_registration_should_be_successful() {
        assertTrue(this.registrationSuccess, "Registration should be successful");
    }
    
    // Common step for checking if registration failed with an error
    @Then("the registration should fail with error {string}")
    public void the_registration_should_fail_with_error(String expectedError) {
        assertFalse(this.registrationSuccess, "Registration should fail");
        assertEquals(expectedError, this.errorMessage);
    }
    
    // Setter methods for shared state
    public void setRegistrationSuccess(boolean success) {
        this.registrationSuccess = success;
    }
    
    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }
    
    // Getter methods to allow other step classes to access the shared state
    public String getCurrentUser() {
        return currentUser;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public boolean isRegistrationSuccess() {
        return registrationSuccess;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    // Import for assertions
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }
    
    private static void assertEquals(String expected, String actual) {
        if (expected == null) {
            if (actual != null) {
                throw new AssertionError("Expected null but was: " + actual);
            }
        } else if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + " but was: " + actual);
        }
    }
}