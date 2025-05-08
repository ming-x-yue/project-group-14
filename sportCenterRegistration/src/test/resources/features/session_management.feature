Feature: Session Management
  As an instructor or owner
  I want to manage sport sessions
  So that customers can register for classes

  Background:
    Given the following accounts exist in the system:
      | username | email          | password  | type       |
      | instruc1 | ins1@email.com | password1 | Instructor |
      | owner1   | own1@email.com | password1 | Owner      |
    And the following sport classes exist in the system:
      | name     | approved |
      | Yoga     | true     |
      | Swimming | true     |

  Scenario: Successfully create a new session
    Given I am logged in as "instruc1"
    When I create a new session with the following details:
      | startTime | endTime  | location | date       | sportClass |
      |  13:00:00 | 14:00:00 | Room A   | 2025-06-01 | Yoga       |
    Then the session should be created successfully
    And the session should appear in the list of available sessions

  Scenario: Cannot create a session with end time before start time
    Given I am logged in as "instruc1"
    When I create a new session with the following details:
      | startTime | endTime  | location | date       | sportClass |
      |  14:00:00 | 13:00:00 | Room A   | 2025-06-01 | Yoga       |
    Then the session creation should fail with error "Session end time cannot be before Session start time!"

  Scenario: Successfully update a session
    Given I am logged in as "instruc1"
    And a session with id "1" exists in the system
    When I update the session with the following details:
      | startTime | endTime  | location | date       |
      |  15:00:00 | 16:00:00 | Room B   | 2025-06-02 |
    Then the session should be updated successfully
    And the session details should be updated in the system

  Scenario: Successfully delete a session
    Given I am logged in as "owner1"
    And a session with id "1" exists in the system
    When I delete the session with id "1"
    Then the session should be deleted successfully
    And the session should not appear in the list of available sessions

  Scenario: View all sessions
    Given I am logged in as "instruc1"
    And the following sessions exist in the system:
      | id | startTime | endTime  | location | date       | instructor | sportClass |
      |  1 |  13:00:00 | 14:00:00 | Room A   | 2025-06-01 | instruc1   | Yoga       |
      |  2 |  15:00:00 | 16:00:00 | Pool     | 2025-06-02 | instruc1   | Swimming   |
    When I request to view all sessions
    Then I should see a list of all sessions
