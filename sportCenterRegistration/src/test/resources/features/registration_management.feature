Feature: Registration Management
  As a customer
  I want to register for sport sessions
  So that I can attend classes

  Background:
    Given the following accounts exist in the system:
      | username  | email           | password  | type       |
      | customer1 | cust1@email.com | password1 | Customer   |
      | instruc1  | ins1@email.com  | password1 | Instructor |
    And the following sport classes exist in the system:
      | name     | approved |
      | Yoga     | true     |
      | Swimming | true     |
    And the following sessions exist in the system:
      | id | startTime | endTime  | location | date       | instructor | sportClass |
      |  1 |  13:00:00 | 14:00:00 | Room A   | 2025-06-01 | instruc1   | Yoga       |
      |  2 |  15:00:00 | 16:00:00 | Pool     | 2025-06-02 | instruc1   | Swimming   |

  Scenario: Successfully register for a session
    Given I am logged in as "customer1"
    When I register for session with id "1"
    Then the registration should be successful
    And I should see session "1" in my registrations

  Scenario: Cannot register for a session that has already ended
    Given I am logged in as "customer1"
    And the current date is after "2025-06-01"
    When I register for session with id "1"
    Then the registration should fail with error "Can not register to a class that has already ended."

  Scenario: Instructor cannot register for their own session
    Given I am logged in as "instruc1"
    When I register for session with id "1"
    Then the registration should fail with error "Instructor can not register to their own session!"

  Scenario: Cannot register for the same session twice
    Given I am logged in as "customer1"
    And I am already registered for session with id "1"
    When I register for session with id "1"
    Then the registration should fail with error "Registration already exists."

  Scenario: Successfully delete a registration
    Given I am logged in as "customer1"
    And I am already registered for session with id "1"
    When I delete my registration for session with id "1"
    Then the deletion should be successful
    And I should not see session "1" in my registrations
