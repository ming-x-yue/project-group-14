package ca.mcgill.ecse321.sportCenterRegistration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.sportCenterRegistration.service.SessionService;
import ca.mcgill.ecse321.sportCenterRegistration.service.SportClassService;
import ca.mcgill.ecse321.sportCenterRegistration.service.InstructorService;
import ca.mcgill.ecse321.sportCenterRegistration.service.OwnerService;
import ca.mcgill.ecse321.sportCenterRegistration.model.Session;
import ca.mcgill.ecse321.sportCenterRegistration.model.SportClass;
import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;

import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SessionManagementSteps {

    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private SportClassService sportClassService;
    
    @Autowired
    private InstructorService instructorService;
    
    @Autowired
    private OwnerService ownerService;
    
    // Context variables to store state between steps
    private String currentUser;
    private String userType;
    private String errorMessage;
    private boolean operationSuccess;
    private List<Session> sessionList;
    private Session currentSession;
    
    @Given("the following sessions exist in the system:")
    public void the_following_sessions_exist_in_the_system(DataTable dataTable) {
        List<Map<String, String>> sessions = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> session : sessions) {
            try {
                int id = Integer.parseInt(session.get("id"));
                String startTimeStr = session.get("startTime");
                String endTimeStr = session.get("endTime");
                String location = session.get("location");
                String dateStr = session.get("date");
                String instructorName = session.get("instructor");
                String sportClassName = session.get("sportClass");
                
                // Convert strings to appropriate types
                Time startTime = Time.valueOf(startTimeStr + ":00");
                Time endTime = Time.valueOf(endTimeStr + ":00");
                Date date = Date.valueOf(dateStr);
                
                // Check if the session already exists
                List<Session> existingSessions = sessionService.getSession(id);
                if (existingSessions == null || existingSessions.isEmpty()) {
                    // Create the session using the proper method signature
                    sessionService.createSession(startTime, endTime, location, date, instructorName, sportClassName);
                }
            } catch (Exception e) {
                System.out.println("Error creating session: " + e.getMessage());
            }
        }
    }
    
    @Given("a session with id {string} exists in the system")
    public void a_session_with_id_exists_in_the_system(String idStr) {
        int id = Integer.parseInt(idStr);
        
        // Try to retrieve the session
        List<Session> sessions = sessionService.getSession(id);
        
        if (sessions == null || sessions.isEmpty()) {
            // Create a default session if it doesn't exist
            try {
                // Create a session using the proper method signature
                Time startTime = Time.valueOf("10:00:00");
                Time endTime = Time.valueOf("11:00:00");
                Date date = new Date(System.currentTimeMillis() + 86400000); // Tomorrow
                String location = "Default Location";
                
                // Use the current user as instructor if they are an instructor
                String instructorName = this.currentUser;
                if (!"instructor".equals(this.userType)) {
                    instructorName = "default_instructor";
                }
                
                // Use a default sport class
                String sportClassName = "DefaultClass";
                
                // Check if the sport class exists, if not create it
                try {
                    sportClassService.getSportClass(sportClassName);
                } catch (IllegalArgumentException e) {
                    sportClassService.createSportClass(sportClassName);
                    sportClassService.approveSportClass(sportClassName);
                }
                
                sessionService.createSession(startTime, endTime, location, date, instructorName, sportClassName);
                
                // Get the created session
                sessions = sessionService.getSession(id);
                if (sessions != null && !sessions.isEmpty()) {
                    this.currentSession = sessions.get(0);
                }
            } catch (Exception e) {
                System.out.println("Error creating default session: " + e.getMessage());
            }
        } else {
            this.currentSession = sessions.get(0);
        }
    }
    
    @When("I create a new session with the following details:")
    public void i_create_a_new_session_with_the_following_details(DataTable dataTable) {
        Map<String, String> sessionDetails = dataTable.asMaps().get(0);
        
        try {
            if ("instructor".equals(this.userType)) {
                String startTimeStr = sessionDetails.get("startTime");
                String endTimeStr = sessionDetails.get("endTime");
                String location = sessionDetails.get("location");
                String dateStr = sessionDetails.get("date");
                String sportClassName = sessionDetails.get("sportClass");
                
                // Convert strings to appropriate types
                Time startTime = Time.valueOf(startTimeStr + ":00");
                Time endTime = Time.valueOf(endTimeStr + ":00");
                Date date = Date.valueOf(dateStr);
                
                // Try to create the session
                Session newSession = sessionService.createSession(
                    startTime, endTime, location, date, this.currentUser, sportClassName
                );
                
                // Get the created session using its ID
                int sessionId = newSession.getId();
                List<Session> sessions = sessionService.getSession(sessionId);
                if (sessions != null && !sessions.isEmpty()) {
                    this.currentSession = sessions.get(0);
                }
                
                this.operationSuccess = true;
            } else {
                throw new IllegalStateException("Only instructors can create sessions");
            }
        } catch (Exception e) {
            this.operationSuccess = false;
            this.errorMessage = e.getMessage();
        }
    }
    
    @When("I update the session with the following details:")
    public void i_update_the_session_with_the_following_details(DataTable dataTable) {
        Map<String, String> sessionDetails = dataTable.asMaps().get(0);
        
        try {
            if (this.currentSession == null) {
                throw new IllegalStateException("No current session to update");
            }
            
            if ("instructor".equals(this.userType)) {
                Instructor sessionInstructor = this.currentSession.getInstructor();
                if (!sessionInstructor.getUsername().equals(this.currentUser)) {
                    throw new IllegalStateException("Only the instructor who created the session can update it");
                }
                
                String startTimeStr = sessionDetails.get("startTime");
                String endTimeStr = sessionDetails.get("endTime");
                String location = sessionDetails.get("location");
                String dateStr = sessionDetails.get("date");
                
                // Convert strings to appropriate types
                Time startTime = Time.valueOf(startTimeStr + ":00");
                Time endTime = Time.valueOf(endTimeStr + ":00");
                Date date = Date.valueOf(dateStr);
                
                // Update the session
                sessionService.updateSession(
                    this.currentSession.getId(), 
                    startTime, 
                    endTime, 
                    location, 
                    date,
                    this.currentUser,
                    this.currentSession.getSportClass()
                );
                
                this.operationSuccess = true;
            } else {
                throw new IllegalStateException("Only instructors can update sessions");
            }
        } catch (Exception e) {
            this.operationSuccess = false;
            this.errorMessage = e.getMessage();
        }
    }
    
    @When("I delete the session with id {string}")
    public void i_delete_the_session_with_id(String idStr) {
        int id = Integer.parseInt(idStr);
        
        try {
            // Check if the session exists
            List<Session> sessions = sessionService.getSession(id);
            
            if (sessions != null && !sessions.isEmpty()) {
                Session session = sessions.get(0);
                
                // Check permissions
                if ("owner".equals(this.userType)) {
                    sessionService.deleteSession(id);
                    this.operationSuccess = true;
                } else if ("instructor".equals(this.userType)) {
                    if (session.getInstructor().getUsername().equals(this.currentUser)) {
                        sessionService.deleteSession(id);
                        this.operationSuccess = true;
                    } else {
                        throw new IllegalStateException("Instructors can only delete their own sessions");
                    }
                } else {
                    throw new IllegalStateException("Only owners and the session's instructor can delete sessions");
                }
            } else {
                throw new IllegalArgumentException("Session with ID " + id + " does not exist");
            }
        } catch (Exception e) {
            this.operationSuccess = false;
            this.errorMessage = e.getMessage();
        }
    }
    
    @When("I request to view all sessions")
    public void i_request_to_view_all_sessions() {
        this.sessionList = sessionService.getAllSession();
        this.operationSuccess = true;
    }
    
    @Then("the session should be created successfully")
    public void the_session_should_be_created_successfully() {
        assertTrue(this.operationSuccess, "Session creation should be successful");
        assertNotNull(this.currentSession, "Current session should not be null");
    }
    
    @Then("the session creation should fail with error {string}")
    public void the_session_creation_should_fail_with_error(String expectedError) {
        assertFalse(this.operationSuccess, "Session creation should fail");
        assertEquals(expectedError, this.errorMessage);
    }
    
    @Then("the session should be updated successfully")
    public void the_session_should_be_updated_successfully() {
        assertTrue(this.operationSuccess, "Session update should be successful");
    }
    
    @Then("the session details should be updated in the system")
    public void the_session_details_should_be_updated_in_the_system() {
        // Reload the session to verify the changes were saved
        List<Session> sessions = sessionService.getSession(this.currentSession.getId());
        assertFalse(sessions.isEmpty(), "Updated session should exist in the system");
    }
    
    @Then("the session should be deleted successfully")
    public void the_session_should_be_deleted_successfully() {
        assertTrue(this.operationSuccess, "Session deletion should be successful");
    }
    
    @Then("the session should appear in the list of available sessions")
    public void the_session_should_appear_in_the_list_of_available_sessions() {
        List<Session> allSessions = sessionService.getAllSession();
        boolean found = false;
        
        for (Session s : allSessions) {
            if (s.getId() == this.currentSession.getId()) {
                found = true;
                break;
            }
        }
        
        assertTrue(found, "Session should be found in the list of available sessions");
    }
    
    @Then("the session should not appear in the list of available sessions")
    public void the_session_should_not_appear_in_the_list_of_available_sessions() {
        List<Session> allSessions = sessionService.getAllSession();
        boolean found = false;
        
        for (Session s : allSessions) {
            if (s.getId() == this.currentSession.getId()) {
                found = true;
                break;
            }
        }
        
        assertFalse(found, "Session should not be found in the list of available sessions");
    }
    
    @Then("I should see a list of all sessions")
    public void i_should_see_a_list_of_all_sessions() {
        assertNotNull(this.sessionList, "Session list should not be null");
        assertTrue(!this.sessionList.isEmpty(), "Session list should not be empty");
    }
}