Feature: Sport Class Management
  As an owner or instructor
  I want to manage sport classes
  So that sessions can be created for these classes

  Background:
    Given the following accounts exist in the system:
      | username | email          | password  | type       |
      | instruc1 | ins1@email.com | password1 | Instructor |
      | owner1   | own1@email.com | password1 | Owner      |

  Scenario: Successfully create a new sport class
    Given I am logged in as "instruc1"
    When I create a new sport class with name "Pilates"
    Then the sport class should be created successfully
    And the sport class should appear in the list of sport classes with approved status "false"

  Scenario: Cannot create a sport class with an existing name
    Given I am logged in as "instruc1"
    And a sport class with name "Yoga" exists in the system
    When I create a new sport class with name "Yoga"
    Then the sport class creation should fail with error "Sport Class already exists!"

  Scenario: Successfully approve a sport class
    Given I am logged in as "owner1"
    And a sport class with name "Pilates" exists in the system with approved status "false"
    When I approve the sport class with name "Pilates"
    Then the sport class should be approved successfully
    And the sport class "Pilates" should have approved status "true"

  Scenario: Successfully delete a sport class
    Given I am logged in as "owner1"
    And a sport class with name "Pilates" exists in the system
    When I delete the sport class with name "Pilates"
    Then the sport class should be deleted successfully
    And the sport class should not appear in the list of sport classes

  Scenario: Cannot delete a non-existent sport class
    Given I am logged in as "owner1"
    When I delete the sport class with name "NonExistent"
    Then the sport class deletion should fail with error "Sport Class doesn't exist!"

  Scenario: View all sport classes
    Given I am logged in as "owner1"
    And the following sport classes exist in the system:
      | name     | approved |
      | Yoga     | true     |
      | Swimming | true     |
      | Pilates  | false    |
    When I request to view all sport classes
    Then I should see a list of all sport classes
